package com.enablix.bulkimport.gdrive;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.content.bulkimport.ImportContext;
import com.enablix.app.content.bulkimport.ImportProcessor;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.content.ImportRecord;
import com.enablix.core.domain.content.ImportRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

@Component
public class GDriveBulkImportProcessor implements ImportProcessor {

	@Value("${google.app.enablix.clientId}")
	private String clientId;
	
	@Value("${google.app.enablix.clientSecret}")
	private String clientSecret;
	
	@Value("${google.app.enablix.redirectUri}")
	private String redirectUri;
	
	@Autowired
	private GoogleDocExportFormat exportFormat;
	
	@Override
	public String importSource() {
		return "GOOGLEDRIVE";
	}

	@Override
	public void validateRequest(ImportRequest request) throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ImportDoc processRecord(ImportRecord record, ImportContext ctx) throws IOException {
		
		Drive drive = getDrive(ctx);
		
		InputStream is = null;
		String filename = (String) record.getSourceRecord().get("name");
		String contentType = (String) record.getSourceRecord().get("mimeType");
		
		String exportType = exportFormat.getMapping().get(record.getSourceRecord().get("mimeType"));
		
		if (exportType != null) {
			
			contentType = exportType;
			
			String extn = exportFormat.getExtension().get(exportType);
			if (StringUtil.hasText(extn) && !filename.toLowerCase().endsWith(extn)) {
				filename = filename + extn;
			}
			
			is = drive.files().export(record.getId(), exportType).executeMediaAsInputStream();
			
		} else {
			is = drive.files().get(record.getId()).executeMediaAsInputStream();
		}
		
		return new ImportDoc(filename, contentType, is); 
	}
	
	private String getOAuthToken(ImportContext ctx) throws IOException {

		String oauthToken = (String) ctx.getSharedData().get("oauth_token");
		
		if (oauthToken == null) {
			
			String authCode = (String) ctx.getRequest().getSourceDetails().get("auth_code");
			
			String redirectUrl = (String) ctx.getRequest().getSourceDetails().get("redirect_url");
			if (!StringUtil.hasText(redirectUrl)) {
				redirectUrl = this.redirectUri;
			}
			
			GoogleTokenResponse tokenResponse =
			          new GoogleAuthorizationCodeTokenRequest(
			        		  Utils.getDefaultTransport(),
			        		  JacksonFactory.getDefaultInstance(),
			        		  "https://www.googleapis.com/oauth2/v4/token",
			        		  clientId, clientSecret,
			        		  authCode, redirectUrl
			        	).execute();
			
			oauthToken = tokenResponse.getAccessToken();
			ctx.getSharedData().put("oauth_token", oauthToken);
		}
		
		return oauthToken;
	}
	
	private Drive getDrive(ImportContext ctx) throws IOException {
		
		String accessToken = getOAuthToken(ctx);
		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		
		return new Drive.Builder(Utils.getDefaultTransport(), 
						Utils.getDefaultJsonFactory(), credential)
					.setApplicationName("Enablix")
					.build();
	}

}
