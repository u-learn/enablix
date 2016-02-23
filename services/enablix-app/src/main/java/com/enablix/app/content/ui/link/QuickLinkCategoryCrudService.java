package com.enablix.app.content.ui.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.link.repo.QuickLinkCategoryRepository;
import com.enablix.app.service.AbstractCrudService;
import com.enablix.core.domain.links.QuickLinkCategory;

@Component
public class QuickLinkCategoryCrudService extends AbstractCrudService<QuickLinkCategory> {

	@Autowired
	private QuickLinkCategoryRepository repo;
	
	@Override
	public QuickLinkCategory findByIdentity(String identity) {
		return repo.findByIdentity(identity);
	}

	@Override
	protected QuickLinkCategory doSave(QuickLinkCategory t) {
		return repo.save(t);
	}

	@Override
	protected QuickLinkCategory merge(QuickLinkCategory t, QuickLinkCategory existing) {
		existing.setName(t.getName());
		return existing;
	}

	@Override
	protected QuickLinkCategory findExisting(QuickLinkCategory t) {
		return repo.findByIdentity(t.getIdentity());
	}

}
