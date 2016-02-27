package com.enablix.app.content.ui.link.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.link.QuickLinkAssociationVO;
import com.enablix.app.content.ui.link.QuickLinks;
import com.enablix.app.content.ui.link.QuickLinksService;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.links.QuickLinkCategory;
import com.enablix.core.domain.links.QuickLinkContent;

@RestController
@RequestMapping("quicklink")
public class QuickLinksController {

	@Autowired
	private QuickLinksService quickLinksService;
	
	@RequestMapping(method = RequestMethod.GET, value="/content")
	public QuickLinks quickLinks() {
		return quickLinksService.getQuickLinks();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/categories/{contentIdentity}/")
	public List<QuickLinkAssociationVO> categoryAssociation(@PathVariable String contentIdentity) {
		return quickLinksService.getAllCategoryAssociation(contentIdentity);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/category")
	public void updateCategory(@RequestBody QuickLinkCategory category) {
		quickLinksService.saveQuickLinkCategory(category);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/add")
	public QuickLinkContent addQuickLink(@RequestBody AddQuickLinkRequest addLink) {
		return quickLinksService.addLinkToCategory(addLink.getQuickLinkCategoryIdentity(), 
				addLink.getLinkContent());
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/category/delete")
	public void deleteQuickLinkCategory(@RequestBody String quickLinkCategoryIdentity) {
		quickLinksService.removeLinkCategory(quickLinkCategoryIdentity);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/delete")
	public void deleteQuickLink(@RequestBody String quickLinkIdentity) {
		quickLinksService.removeLinkFromCategory(quickLinkIdentity);
	}
	
	public static class AddQuickLinkRequest {
		
		private String quickLinkCategoryIdentity;
		private ContentDataRef linkContent;
		
		public String getQuickLinkCategoryIdentity() {
			return quickLinkCategoryIdentity;
		}
		
		public void setQuickLinkCategoryIdentity(String quickLinkCategoryIdentity) {
			this.quickLinkCategoryIdentity = quickLinkCategoryIdentity;
		}
		
		public ContentDataRef getLinkContent() {
			return linkContent;
		}
		
		public void setLinkContent(ContentDataRef linkContent) {
			this.linkContent = linkContent;
		}
		
	}
	
}
