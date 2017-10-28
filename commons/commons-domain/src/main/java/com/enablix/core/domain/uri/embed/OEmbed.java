package com.enablix.core.domain.uri.embed;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OEmbed {

	private String type;
	
	private String version;
	
	private String title;
	
	private String authorName;
	
	private String authorUrl;
	
	private String providerName;
	
	private String providerUrl;
	
	private Long cacheAge;
	
	private String thumbnailUrl;
	
	private Integer thumbnailWidth;
	
	private Integer thumbnailHeight;
	
	private String url;
	
	private String html;
	
	private Integer width;
	
	private Integer height;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public Long getCacheAge() {
		return cacheAge;
	}

	public void setCacheAge(Long cacheAge) {
		this.cacheAge = cacheAge;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public Integer getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public Integer getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "OEmbed [type=" + type + ", version=" + version + ", title=" + title + ", authorName=" + authorName
				+ ", authorUrl=" + authorUrl + ", providerName=" + providerName + ", providerUrl=" + providerUrl
				+ ", cacheAge=" + cacheAge + ", thumbnailUrl=" + thumbnailUrl + ", thumbnailWidth=" + thumbnailWidth
				+ ", thumbnailHeight=" + thumbnailHeight + ", url=" + url + ", html=" + html + ", width=" + width
				+ ", height=" + height + "]";
	}
	
}
