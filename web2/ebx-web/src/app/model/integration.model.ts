export class DocStoreMetadata {
  
  static metadata = [
    {
      "storeTypeName" : "Dropbox",
      "storeTypeCode" : "DROPBOX",
      "params" : [
        {
          "paramKey" : "APP_NAME",
          "paramName" : "Application Name",
          "required" : true
        },
        {
          "paramKey" : "ACCESS_TOKEN",
          "paramName" : "Dropbox Access Token",
          "required" : true
        }
      ]
    },
    {
      "storeTypeName" : "WebDAV",
      "storeTypeCode" : "WEBDAV",
      "params" : [
        {
          "paramKey" : "HOST",
          "paramName" : "Server Host",
          "required" : true
        },
        {
          "paramKey" : "BASE_DOC_PATH",
          "paramName" : "Base document path",
          "required" : true
        },
        {
          "paramKey" : "USERNAME",
          "paramName" : "Username",
          "required" : true
        },
        {
          "paramKey" : "PASSWORD_ENC",
          "paramName" : "Password",
          "required" : true
        }
      ]
    },
    {
      "storeTypeName" : "Sharepoint",
      "storeTypeCode" : "SHAREPOINT",
      "params" : [
        {
          "paramKey" : "SITE_URL",
          "paramName" : "Sharepoint Site Url",
          "required" : true
        },
        {
          "paramKey" : "BASE_FOLDER",
          "paramName" : "Base Folder",
          "required" : true
        },
        {
          "paramKey" : "USERNAME",
          "paramName" : "Username",
          "required" : true
        },
        {
          "paramKey" : "PASSWORD_ENC",
          "paramName" : "Password",
          "required" : true
        }
      ]
    },
    {
      "storeTypeName" : "Google Drive",
      "storeTypeCode" : "GOOGLEDRIVE",
      "params" : [
        {
          "paramKey" : "BASE_FOLDER",
          "paramName" : "Base Folder",
          "required" : true
        },
        {
          "paramKey" : "AUTH_KEY_FILE_ENC",
          "paramName" : "Service Account Key File",
          "paramType" : "TEXT_FILE_CONTENT",
          "required" : true
        }
      ]
    }
  ];
}

export class TPIntegrationMetadata {
  
  static metadata = [
    {
      tpKey: "integration.wordpress",
      tpName: "Wordpress Integration",
      params: [
        {
          paramKey: "BASE_URL",
          paramName: "Base Url"
        }
      ]
    }
  ];
}