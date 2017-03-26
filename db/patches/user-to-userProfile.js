/**************************************************************
Script to set actor name in audit records

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name> 

****************************************************************/
// Switch to admin database and get list of databases.
db = db.getSiblingDB("admin");
dbs = db.runCommand({
    "listDatabases": 1
}).databases;

// Get the roles associated with the user
var getRoles = function(userId, tenantDb) {
    userRoles = tenantDb.ebx_user_role.findOne({
        "userIdentity": userId
    });
    var roles = userRoles ? userRoles.roles : [];
    print("Roles are  " + roles + " for User Id :: " + userId + " for the tenant  :: " + tenantDb);
    return roles;
};

var getReferenceData = function(userId, tenantDb, templateId) {
    var userTemplateCollection = templateId + "_user";
    userTemplateDoc = tenantDb.getCollection(userTemplateCollection).findOne({
        "email": userId
    }, {
        _id: 0,
        _class: 0,
        createdByName: 0,
        __container: 0,
        modifiedAt: 0,
        userName: 0,
        createdAt: 0,
        modifiedByName: 0,
        createdBy: 0,
        identity: 0,
        __title: 0,
        modifiedBy: 0,
		email: 0
    });
    return userTemplateDoc;
};

//Iterate through each database and get its collections.
dbs.forEach(function(database) {

    // only run the patch for client databases
    if (database.name.endsWith("_enablix") &&
        database.name != "system_enablix") {

        var tenantId = database.name.substring(0, database.name.indexOf("_enablix"));

        // get all tenant users from system database
        systemDb = db.getSiblingDB("system_enablix");
        var tenantUsers = systemDb.ebxUser.find({
            "tenantId": tenantId
        });

        db = db.getSiblingDB(database.name);

        var templateRecords = db.templateDocument.find({});

        var template = templateRecords[0].template;

        print(" Tenant Id :: " + tenantId + " Database Name :: " + db.name + " Template Name  :: " + template._id);

        tenantUsers.forEach(function(usr) {

            if (usr.profile != undefined) {

                print(" Processing the user " + usr.userId);

                var roles = getRoles(usr.identity, db);
                var referenceData = getReferenceData(usr.userId, db, template._id);

                db.ebx_user_profile.insertOne({
                    "_id": usr._id,
                    "_class": "com.enablix.core.domain.user.UserProfile",
                    "name": usr.profile.name,
                    "email": usr.userId,
                    "systemProfile": {
                        "roles": roles,
                        "sendWeeklyDigest": usr.profile.sendWeeklyDigest
                    },
                    "businessProfile": {
                        "attributes": referenceData

                    },
                    "identity": usr.identity,
                    "createdAt": usr.createdAt,
                    "createdBy": usr.createdBy,
                    "createdByName": usr.createdBy,
                    "modifiedBy": usr.modifiedBy,
                    "modifiedByName": usr.modifiedBy,
                    "modifiedAt": usr.modifiedAt
                })
            }
        });
    }
});