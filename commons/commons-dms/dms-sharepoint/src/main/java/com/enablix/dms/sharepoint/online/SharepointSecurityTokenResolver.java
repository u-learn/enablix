package com.enablix.dms.sharepoint.online;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;

import com.enablix.commons.dms.credentials.UsernamePasswordCredentials;
import com.enablix.commons.dms.credentials.UsernamePasswordCredentialsBuilder;
import com.enablix.core.domain.config.Configuration;
import com.enablix.dms.sharepoint.SharepointConstants;
import com.enablix.dms.sharepoint.SharepointException;
import com.enablix.dms.sharepoint.SharepointUtil;

@Component
public class SharepointSecurityTokenResolver {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${sharepoint.online.login.url:https://login.microsoftonline.com/extSTS.srf}")
	private String loginEndpointUrl;
	
	@Autowired
	private UsernamePasswordCredentialsBuilder credentialsBuilder;
	
	private XPathExpression xPathExpression = XPathExpressionFactory.createXPathExpression(
			"/S:Envelope/S:Body/wst:RequestSecurityTokenResponse/wst:RequestedSecurityToken/wsse:BinarySecurityToken", 
			SharepointUtil.sharepointNamespaces());
	
	public String getSecurityToken(Configuration config) throws TransformerFactoryConfigurationError, SharepointException, URISyntaxException, TransformerException {
		
		UsernamePasswordCredentials credentials = credentialsBuilder.buildCredentials(config);
		String siteUrl = config.getStringValue(SharepointConstants.CFG_SP_SITE_URL_KEY);
		
		String tokenRequestEnvelope = buildSecurityTokenRequestEnvelope(credentials.getUsername(), credentials.getPassword(), siteUrl);
		
		RequestEntity<String> requestEntity = new RequestEntity<>(tokenRequestEnvelope, HttpMethod.POST, new URI(loginEndpointUrl));
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

		DOMResult result = new DOMResult();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new StringSource(responseEntity.getBody()), result);
		Document definitionDocument = (Document) result.getNode();

		String securityToken = xPathExpression.evaluateAsString(definitionDocument);
		
		if (!StringUtils.hasText(securityToken)) { 
			throw new SharepointException("Unable to authenticate: empty token");
		}
		
		return securityToken;
	}
	
	private String buildSecurityTokenRequestEnvelope(String username, String password, String siteUrl) {
		
		String endpoint = SharepointUtil.getSPEndpointAddress(siteUrl);
		
		return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\">" +
					"<s:Header>" +
						"<a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>" + 
						"<a:ReplyTo>" +
							"<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>" +
						"</a:ReplyTo>" + 
						"<a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To>" + 
						"<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
							"<o:UsernameToken>" +
								"<o:Username>" + username + "</o:Username>" +
								"<o:Password>" + password + "</o:Password>" +
							"</o:UsernameToken>" +
						"</o:Security>" +
					"</s:Header>" +
					"<s:Body>" +
						"<t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">" +
							"<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">" +
								"<a:EndpointReference>" +
									"<a:Address>" + endpoint + "</a:Address>" +
								"</a:EndpointReference>" +
							"</wsp:AppliesTo>" + 
							"<t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType>" +
							"<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType>" +
							"<t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType>" +
						"</t:RequestSecurityToken>" +
					"</s:Body>" +
				"</s:Envelope>";
	}
	
}
