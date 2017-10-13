package com.enablix.hubspot;

import org.springframework.stereotype.Component;

import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocRef;
import com.enablix.core.ui.Hyperlink;
import com.enablix.hubspot.model.ActionHook;
import com.enablix.hubspot.model.HubspotAction;
import com.enablix.hubspot.model.HubspotConstants;
import com.enablix.hubspot.model.HubspotContent;
import com.enablix.hubspot.model.HubspotContent.Link;

@Component
public class DisplayableToHubspotContentTx {

	public HubspotContent transform(DisplayableContent in, long objectId) {
		
		HubspotContent hbContent = new HubspotContent();

		hbContent.setObjectId(objectId);
		hbContent.setTitle(in.getTitle());
		hbContent.setLink(in.getPortalUrl());
		hbContent.setContentType(in.getContainerLabel());
		hbContent.setDownloadLink(getDocDownloadProp(in));
		hbContent.setExtUrlLink(getExtLinkProp(in));
		
		/*HubspotAction downloadAction = getDocDownloadAction(in);
		if (downloadAction != null) {
			hbContent.getActions().add(downloadAction);
		}
		
		HubspotAction urlAction = getExtLinkNavAction(in);
		if (urlAction != null) {
			hbContent.getActions().add(urlAction);
		}*/
		
		return hbContent;
	}
	
	private Link getDocDownloadProp(DisplayableContent in) {
		
		Link link = null;
		
		DocRef doc = in.getDoc();
		if (doc != null) {
			link = new Link(doc.getAccessUrl(), doc.getName());
		}
		
		return link;
	}
	
	private Link getExtLinkProp(DisplayableContent in) {
		
		Link link = null;
		
		Hyperlink hyperlink = in.getHyperlink();
		if (hyperlink != null) {
			link = new Link(hyperlink.getHref(), hyperlink.getTitle());
		}
		
		return link;
	}

	private HubspotAction getDocDownloadAction(DisplayableContent in) {
		
		ActionHook action = null;
		
		DocRef doc = in.getDoc();
		if (doc != null) {
			action = new ActionHook(HubspotConstants.DOWNLOAD_ACTION_LABEL, doc.getAccessUrl());
		}
		
		return action;
	}

	private HubspotAction getExtLinkNavAction(DisplayableContent in) {
		
		ActionHook action = null;
		
		Hyperlink hyperlink = in.getHyperlink();
		if (hyperlink != null) {
			action = new ActionHook(HubspotConstants.NAV_TO_URL_ACTION_LABEL, hyperlink.getHref());
		}
		
		return action;
	}
	
}
