package com.enablix.app.content.enrich;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.TextLinkifier;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.content.EmbeddedUrl;
import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.EmbedService;

@Component
public class ContentUrlEnricher implements ContentEnricher {

	@Autowired
	private EmbedService embedService;
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateFacade contentTemplate) {
		List<EmbeddedUrl> embedUrls = findEmbeddedUrls(updateCtx.contentQId(), content, contentTemplate);
		content.put(ContentDataConstants.CONTENT_URLS_KEY, embedUrls);
	}

	public List<EmbeddedUrl> findEmbeddedUrls(String contentQId, Map<String, Object> content, TemplateFacade contentTemplate) {
		
		Collection<String> urls = new LinkedHashSet<>();
		List<EmbeddedUrl> embedUrls = new ArrayList<>();
		
		ContainerType containerDef = contentTemplate.getContainerDefinition(contentQId);
		
		if (containerDef != null) {
			
			for (ContentItemType contentItem : containerDef.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.TEXT || 
						contentItem.getType() == ContentItemClassType.RICH_TEXT) {
				
					String text = (String) content.get(contentItem.getId());
					
					if (StringUtil.hasText(text)) {
						urls.addAll(TextLinkifier.parseUrls(text));
					}
				}
			}
		}

		urls.forEach((url) -> {
			
			EmbedInfo embedInfo = embedService.getEmbedInfo(url);
			
			EmbeddedUrl eUrl = EmbeddedUrl.fromEmbedInfo(embedInfo);
			if (eUrl != null) {
				embedUrls.add(eUrl);
			}
		});
		
		return embedUrls;
	}

}
