package com.enablix.app.content.doc.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocEmbedHtmlResolver;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocFormatResolver;

@Component
public class MSOfficeDocEmbedHtmlResolver implements DocEmbedHtmlResolver {

	private Set<DocumentFormat> supportedFormats;
	
	public MSOfficeDocEmbedHtmlResolver() {

		this.supportedFormats = new HashSet<>();
		
		this.supportedFormats.addAll(Arrays.asList(
				DocumentFormat.DOC, DocumentFormat.DOCX, 
				DocumentFormat.PPT, DocumentFormat.PPTX, 
				DocumentFormat.XLS, DocumentFormat.XLSX
		));
	}
	
	@Value("${msoffice.doc.viewer.embed.html}")
	private String embedHtmlTemplate;
	
	@Autowired
	private DocFormatResolver docFormatResolver;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Override
	public String getEmbedHtml(DocumentMetadata docMd) {
		String docDownloadUrl = EnvPropertiesUtil.getDefaultServerUrl() + 
				docUrlPopulator.getActualDocDownloadUrl(docMd.getIdentity(), new DisplayContext());
		return embedHtmlTemplate.replaceFirst(":docUrl", docDownloadUrl);
	}

	@Override
	public boolean canHandle(DocumentMetadata docMd) {
		DocumentFormat format = docFormatResolver.resolve(docMd);
		return format != null && supportedFormats.contains(format);
	}

}
