/**************************************************************
Script to remove old/obsolete records from ebx_user_content_relevance collection 

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> --eval "var fromUserId = '<oldUserId>', toUserId = '<newUserId>';" <path>/<script-file-name>

****************************************************************/


db = db.getSiblingDB("system_enablix");
var user = db.ebxUser.findOne({userId: fromUserId});

if (fromUserId && toUserId && user) {
	
	db.ebxUser.update(
		{ userId: fromUserId },
		{ $set: {
				userId: toUserId,
				identity: toUserId
			}
		}
	);
	
	db.ebxUser.update(
		{ createdBy: fromUserId },
		{ $set: { createdBy: toUserId }},
		{ multi: true }
	);
	
	db.ebxUser.update(
			{ modifiedBy: fromUserId },
			{ $set: { modifiedBy: toUserId }},
			{ multi: true }
		);
	
	db.ebx_shared_site_url.update(
			{ createdBy: fromUserId },
			{ $set: { createdBy: toUserId }},
			{ multi: true }
		);
	
	db.ebx_shared_site_url.update(
			{ modifiedBy: fromUserId },
			{ $set: { modifiedBy: toUserId }},
			{ multi: true }
		);
	
	// switch to tenant specific database
	db = db.getSiblingDB(user.tenantId + "_enablix");
	
	db.ebx_user_profile.update(
			{ email: fromUserId },
			{ $set: {
					email: toUserId,
					userIdentity: toUserId,
					identity: toUserId
				}
			}
		);
	
	db.ebx_user_content_relevance.update(
			{ userId: fromUserId },
			{ $set: { userId: toUserId } },
			{ multi: true }
		);
	
	db.ebx_activity_audit.update(
			{ "actor.userId": fromUserId },
			{ $set: { "actor.userId": toUserId } },
			{ multi: true }
		);
	
	db.ebx_content_approval.update(
			{ "objectRef.data.createdBy": fromUserId },
			{ $set: { "objectRef.data.createdBy": toUserId } },
			{ multi: true }
		);
	
	db.ebx_content_approval.update(
			{ "objectRef.data.modifiedBy": fromUserId },
			{ $set: { "objectRef.data.modifiedBy": toUserId } },
			{ multi: true }
		);
	
	db.ebx_content_approval.find({ "actionHistory.actions.actorUserId": fromUserId }).snapshot().forEach(
		function(rec) {
			rec.actionHistory.actions.forEach(function(act) {
				if (act.actorUserId == fromUserId) {
					act.actorUserId = toUserId;
				}
			});
			db.ebx_content_approval.save(rec);
		}
	);
	
	db.ebx_item_user_correlation.update(
			{ "userProfileIdentity": fromUserId },
			{ $set: { "userProfileIdentity": toUserId } },
			{ multi: true }
		);
	
	db.ebx_user_preference.update(
			{ "userId": fromUserId },
			{ $set: { "userId": toUserId } },
			{ multi: true }
		);
	
	db.ebx_user_task.update(
			{ "userId": fromUserId },
			{ $set: { "userId": toUserId } },
			{ multi: true }
		);
	
	db.getCollectionNames().forEach(function(c) {
		
		db.getCollection(c).update(
			{ createdBy: fromUserId },
			{ $set: { createdBy: toUserId }},
			{ multi: true }
		);
		
		db.getCollection(c).update(
				{ modifiedBy: fromUserId },
				{ $set: { modifiedBy: toUserId }},
				{ multi: true }
			);
		
	});
}

