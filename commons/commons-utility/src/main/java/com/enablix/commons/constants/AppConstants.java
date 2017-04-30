package com.enablix.commons.constants;

public class AppConstants {

	public static final String BASE_DB_NAME = "enablix";
	public static final String SYSTEM_DB_NAME = "system_enablix";
	
	public static final String SYSTEM_USER_COLL_NAME = "ebxUser";
	public static final String TENANT_ID_ATTR_ID = "tenantId";

	public static final String ITEM_USER_CORR_COLL_NAME = "ebx_item_user_correlation";
	public static final String SYSTEM_USER_ID = "system";
	public static final String SYSTEM_USER_NAME = "System";
	
	public static final String GUEST_USER_IDENTITY = "~guest~";
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	public static final String TENANT_ID_REQ_PARAM = "tenantId";
	public static final String CLIENT_ID_REQ_PARAM = "clientId";
	
	public static final String USER_PROFILE_COLL_NAME = "ebx_user_profile";
	
	public static final String SYSTEM_SLACK_TOKEN = "ebxSlackTokenDtls";
	public static final String SLACK_APP = "SLACK_APP";
	public static final String DOC_DOWN_TRACK_SLACK = "?atChannel=SLACK";
	
	public static final String SLACK_APP_CLIENT_ID = "clientID";
	public static final String SLACK_APP_CLIENT_SECRET = "clientSecret";
	public static final String SLACK_RESPONSE_OK = "ok";
	public static final String SLACK_ERROR_TOKEN_REVOKED = "token_revoked";
	public static final String SLACK_ERROR = "error";
	public static final String SLACK_REQUEST_TOKEN = "token";
	public static final String SLACK_REQUEST_REDIRECT_URI = "redirect_uri";
	public static final String SLACK_RESPONSE_USER = "user";
	public static final String SLACK_REQUEST_ATTACHMENT = "attachments";
	public static final String SLACK_REQUEST_TEXT = "text";
	public static final String SLACK_REQUEST_CHANNEL = "channel";
	public static final String SLACK_REQUEST_ARCHIVED = "exclude_archived";
	public static final String SLACK_REQUEST_CODE = "code";
	public static final String SLACK_REQUEST_CLIENT_SEC = "client_secret";
	public static final String SLACK_REQUEST_CLIENT_ID = "client_id";
	
	// Data view constants
	public static final String MONGO_DATASTORE = "MONGO";
	public static final String ELASTICSEARCH_DATASTORE = "ELASTICSEARCH";
	public static final String EVAL = "eval";
	public static final String ACTIVITY_METRIC_CONFIG = "ebx_activity_metric_config";
	public static final String ACTIVITY_METRIC = "ebx_activity_metric";
	public static final String ACTIVITY_TREND_KEY = "Time";
	
	public enum MetricTypes{
		NO_OF_LOGINS("No Of Logins","ACTMETRIC2"),
		CONTENT_ADD("Content Add","ACTMETRIC4"),
		CONTENT_UPDATES("Content Updates","ACTMETRIC5"),
		CONTENT_ACCESS("Content Access","ACTMETRIC6"),
		CONTENT_PREVIEW("Content Preview","ACTMETRIC7"),
		CONTENT_DOWNLOAD("Content Download","ACTMETRIC8"),
		CONTENT_ACC_WEEKLY_DIGEST("Content Access via Weekly Digest","ACTMETRIC10"),
		DISTINCT_LOGIN("Number of Distinct Logins","ACTMETRIC3");
		
		private String value;
		private String code;
		
		private MetricTypes(String value, String code){
			this.value=value;
			this.code=code;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public String getCode() {
			return code;
		}
	}
}