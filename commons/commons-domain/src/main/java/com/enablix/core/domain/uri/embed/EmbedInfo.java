package com.enablix.core.domain.uri.embed;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection="ebx_embed_info")
public class EmbedInfo extends BaseDocumentEntity {

	private String type;
	
	private String url;
	
	private String site;
	
	private String title;
	
	private String description;
	
	private Favicon favicon;
	
	private List<Image> images;
	
	private List<Video> videos;
	
	private List<Audio> audios;
	
	private OEmbed oembed;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Favicon getFavicon() {
		return favicon;
	}

	public void setFavicon(Favicon favicon) {
		this.favicon = favicon;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public List<Audio> getAudios() {
		return audios;
	}

	public void setAudios(List<Audio> audios) {
		this.audios = audios;
	}

	public OEmbed getOembed() {
		return oembed;
	}

	public void setOembed(OEmbed oembed) {
		this.oembed = oembed;
	}

	@Override
	public String toString() {
		return "EmbedInfo [type=" + type + ", url=" + url + ", site=" + site + ", title=" + title + ", description="
				+ description + ", favicon=" + favicon + ", images=" + images + ", videos=" + videos + ", audios="
				+ audios + ", oembed=" + oembed + "]";
	}
	
	
	
}
