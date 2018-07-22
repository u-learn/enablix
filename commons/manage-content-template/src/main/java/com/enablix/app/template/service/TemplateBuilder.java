package com.enablix.app.template.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType.HeadingContentItem;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContainerUIDefType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.ContentUIDefType;
import com.enablix.core.commons.xsdtopojo.RefContentItemType;
import com.enablix.core.commons.xsdtopojo.TextAutoPopulateType;
import com.enablix.core.commons.xsdtopojo.TextUIClassType;
import com.enablix.core.commons.xsdtopojo.TextUIDefType;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.ContainerVisitor;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

public class TemplateBuilder {

	private static final int DEFAULT_TEXT_SEARCH_BOOST = 7;

	private static final String NAME_ATTR_ID = "name";
	private static final String NAME_ATTR_LBL = "Name";

	private static final String SHORT_NAME_ATTR_ID = "shortName";
	private static final String SHORT_NAME_ATTR_LBL = "Short Name";

	private static final String TITLE_ATTR_ID = "title";
	private static final String TITLE_ATTR_LBL = "Title";
	
	private static final String DESC_ATTR_ID = "desc";
	private static final String DESC_ATTR_LBL = "Description";
	
	private static final String URL_ATTR_ID = "url";
	private static final String URL_ATTR_LBL = "URL";
	
	private static final String FILE_ATTR_ID = "file";
	private static final String FILE_ATTR_LBL = "File";

	public static final Set<String> BIZ_DIM_COLORS = new LinkedHashSet<>();
	
	static {
		BIZ_DIM_COLORS.add("#e75632");
		BIZ_DIM_COLORS.add("#7147d3");
		BIZ_DIM_COLORS.add("#4046ad");
		BIZ_DIM_COLORS.add("#d0a914");
		BIZ_DIM_COLORS.add("#a62078");
		BIZ_DIM_COLORS.add("#70e58f");
		BIZ_DIM_COLORS.add("#448e14");
		BIZ_DIM_COLORS.add("#dbda1e");
		BIZ_DIM_COLORS.add("#569cb1");
		BIZ_DIM_COLORS.add("#6a1c9e");
		BIZ_DIM_COLORS.add("#e0129f");
		BIZ_DIM_COLORS.add("#c87b89");
		BIZ_DIM_COLORS.add("#8fcc92");
		BIZ_DIM_COLORS.add("#75684c");
		BIZ_DIM_COLORS.add("#c81dfa");
		BIZ_DIM_COLORS.add("#857610");
		BIZ_DIM_COLORS.add("#d4b577");
		BIZ_DIM_COLORS.add("#9c789b");
		BIZ_DIM_COLORS.add("#ee7aaa");
		BIZ_DIM_COLORS.add("#e6bcdc");
		BIZ_DIM_COLORS.add("#bda2e6");
		BIZ_DIM_COLORS.add("#6a905e");
		BIZ_DIM_COLORS.add("#c23c0b");
		BIZ_DIM_COLORS.add("#55ae52");
		BIZ_DIM_COLORS.add("#b6410a");
		BIZ_DIM_COLORS.add("#82514a");
		BIZ_DIM_COLORS.add("#66818b");
		BIZ_DIM_COLORS.add("#4968a3");
		BIZ_DIM_COLORS.add("#be203f");
		BIZ_DIM_COLORS.add("#0ee4b2");
		BIZ_DIM_COLORS.add("#058bea");
		BIZ_DIM_COLORS.add("#e6979d");
		BIZ_DIM_COLORS.add("#d27862");
		BIZ_DIM_COLORS.add("#18536c");
		BIZ_DIM_COLORS.add("#968283");
		BIZ_DIM_COLORS.add("#bdd0c0");
		BIZ_DIM_COLORS.add("#179ac5");
		BIZ_DIM_COLORS.add("#d9004f");
		BIZ_DIM_COLORS.add("#befb21");
		BIZ_DIM_COLORS.add("#c39989");
	}
	
	
	public static boolean updateContainerAndCheckLabelUpdate(ContentTemplate template, ContainerType container) throws Exception {
		
		ContainerType existContainer = TemplateUtil.findContainer(template.getDataDefinition(), container.getQualifiedId());
		
		if (existContainer == null) {
			throw new ValidationException(Collections.singletonList(
					new AppError(ErrorCodes.INVALID_CONTAINER_QID, "Invalid container [" + container.getQualifiedId() + "]")));
		}
		
		boolean labelUpdated = !container.getLabel().equals(existContainer.getLabel())
								|| (container.getSingularLabel() != null 
								&& !container.getSingularLabel().equals(existContainer.getSingularLabel()));
		
		BeanUtils.copyProperties(container, existContainer);
		
		TemplateContainerWalker templateWalker = new TemplateContainerWalker(template, 
				(visitContainer) -> !visitContainer.getQualifiedId().equals(container.getQualifiedId()));
		
		templateWalker.walk((visitContainer) -> {
			
			// update label in linked containers
			visitContainer.getContainer().forEach((subContainer) -> {
				if (TemplateUtil.isLinkedContainer(subContainer)
						&& subContainer.getLinkContainerQId().equals(container.getQualifiedId())) {
					subContainer.setLabel(container.getLabel());
				}
			});
			
			// update label in content items
			visitContainer.getContentItem().forEach((ci) -> {
				
				if (ci.getBounded() != null && ci.getBounded().getRefList() != null
						&& ci.getBounded().getRefList().getDatastore() != null
						&& ci.getBounded().getRefList().getDatastore().getStoreId().equals(container.getQualifiedId())) {
					
					ci.setLabel(container.getLabel());
				}
				
			});
		});
		
		return labelUpdated;
	}
	
	public static void addContainerToTemplate(ContentTemplate template, ContainerType container) throws Exception {
		
		template.getDataDefinition().getContainer().add(container);
		
		if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION) {
			
			addTextItem(NAME_ATTR_ID, NAME_ATTR_LBL, container);
			addTextItem(SHORT_NAME_ATTR_ID, SHORT_NAME_ATTR_LBL, container);
			
			assignColorToContainer(template, container);

			addUILabelConfig(template, container, SHORT_NAME_ATTR_ID);
			
			addShortNameUIConfig(template, container);
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template, 
					(cntnrType) -> cntnrType.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_CONTENT);
			
			walker.walk((bizContentContainer) -> {
				addLinkedContainer(container, SHORT_NAME_ATTR_ID, bizContentContainer);
			});
			
		} else if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_CONTENT) {
			
			// Add title field - text
			addTextItem(TITLE_ATTR_ID, TITLE_ATTR_LBL, container);
			
			// add title UI config as label
			addUILabelConfig(template, container, TITLE_ATTR_ID);

			// Add file field - doc
			addDocItem(FILE_ATTR_ID, FILE_ATTR_LBL, container);
			
			// Add url field - text
			addContentItem(URL_ATTR_ID, URL_ATTR_LBL, ContentItemClassType.TEXT, container);
			
			// Add desc field - richText
			addRichTextItem(DESC_ATTR_ID, DESC_ATTR_LBL, container);
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template, 
					(cntnrType) -> cntnrType.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION);
			
			walker.walk((bizDimContainer) -> {
				String lblAttrId = TemplateUtil.getPortalLabelAttributeId(template, bizDimContainer.getQualifiedId());
				addLinkedContainer(bizDimContainer, lblAttrId, container);
			});
		}
		
	}

	private static void assignColorToContainer(ContentTemplate template, ContainerType container) {
		
		Set<String> usedColors = new HashSet<>();
		
		for (ContainerType tmpCont : template.getDataDefinition().getContainer()) {
			if (StringUtil.hasText(tmpCont.getColor())) {
				usedColors.add(tmpCont.getColor());
			}
		}
		
		String color = getNextBizDimColor(usedColors);
		if (StringUtil.hasText(color)) {
			container.setColor(color);
		}
		
	}

	private static void addLinkedContainer(ContainerType inContainer, 
			String inContainerLabelAttrId, ContainerType linkContainer) {
		
		String linkContentItemId = linkContainer.getId() + inContainer.getId();
		String linkContainerId = inContainer.getId() + linkContainer.getId();
		
		// Add linked content item in biz container
		ContentItemType linkingItem = new ContentItemType();
		linkingItem.setId(linkContentItemId);
		linkingItem.setQualifiedId(QIdUtil.createQualifiedId(linkContainer.getQualifiedId(), linkContentItemId));
		linkingItem.setLabel(inContainer.getLabel());
		linkingItem.setType(ContentItemClassType.BOUNDED);
		
		BoundedType boundedType = new BoundedType();
		boundedType.setMultivalued(true);
		BoundedRefListType refListType = new BoundedRefListType();
		
		BoundedListDatastoreType ds = new BoundedListDatastoreType();
		ds.setHyperlink(true);
		ds.setStoreId(inContainer.getQualifiedId());
		ds.setDataId(ContentDataConstants.IDENTITY_KEY);
		ds.setDataLabel(inContainerLabelAttrId);
		refListType.setDatastore(ds);
		
		boundedType.setRefList(refListType );
		
		linkingItem.setBounded(boundedType);
		
		linkContainer.getContentItem().add(linkingItem);
		
		ContainerType linkedContentType = new ContainerType();
		linkedContentType.setLabel(linkContainer.getLabel());
		linkedContentType.setId(linkContainerId);
		linkedContentType.setQualifiedId(QIdUtil.createQualifiedId(inContainer.getId(), linkContainerId));
		linkedContentType.setLinkContainerQId(linkContainer.getQualifiedId());
		linkedContentType.setLinkContentItemId(linkContentItemId);
		
		inContainer.getContainer().add(linkedContentType);
	}

	private static void addShortNameUIConfig(ContentTemplate template, ContainerType container) {
		
		ContentUIDefType shortNameConfig = new ContentUIDefType();
		shortNameConfig.setQualifiedId(QIdUtil.createQualifiedId(container.getQualifiedId(), SHORT_NAME_ATTR_ID));
		
		TextUIDefType snTextUIDef = new TextUIDefType();
		snTextUIDef.setType(TextUIClassType.SHORT);
		
		TextAutoPopulateType snAutoPopulate = new TextAutoPopulateType();
		RefContentItemType nameRefItem = new RefContentItemType();
		nameRefItem.setItemId(NAME_ATTR_ID);
		snAutoPopulate.setRefContentItem(nameRefItem );
		
		snTextUIDef.setAutoPopulate(snAutoPopulate );
		shortNameConfig.setText(snTextUIDef );
		
		template.getUiDefinition().getContentUIDef().add(shortNameConfig);
	}

	private static void addUILabelConfig(ContentTemplate template, ContainerType container, String lblAttrId) {
		
		ContentUIDefType contUIDef = new ContentUIDefType();
		contUIDef.setQualifiedId(container.getQualifiedId());
		
		ContainerUIDefType cntnrUIDef = new ContainerUIDefType();
		String shortNameQId = QIdUtil.createQualifiedId(container.getQualifiedId(), lblAttrId);
		cntnrUIDef.setLabelQualifiedId(shortNameQId);
		contUIDef.setContainer(cntnrUIDef );
		
		ContainerPortalConfigType cntnrPortalConfig = new ContainerPortalConfigType();
		HeadingContentItem headingItem = new HeadingContentItem();
		headingItem.setId(lblAttrId);
		
		cntnrPortalConfig.setHeadingContentItem(headingItem);
		
		cntnrUIDef.setPortalConfig(cntnrPortalConfig);
		
		template.getUiDefinition().getContentUIDef().add(contUIDef);
	}

	private static void addTextItem(String attrId, String attrLbl, ContainerType container) {
		addTextItem(attrId, attrLbl, container, DEFAULT_TEXT_SEARCH_BOOST);
	}
	
	private static void addTextItem(String attrId, String attrLbl, ContainerType container, int searchBoost) {
		ContentItemType textItem = new ContentItemType();
		textItem.setId(attrId);
		textItem.setLabel(attrLbl);
		textItem.setType(ContentItemClassType.TEXT);
		textItem.setSearchBoost(BigInteger.valueOf(searchBoost));
		
		container.getContentItem().add(textItem);
	}
	
	private static void addRichTextItem(String attrId, String attrLbl, ContainerType container) {
		addContentItem(attrId, attrLbl, ContentItemClassType.RICH_TEXT, container);
	}
	
	private static void addDocItem(String attrId, String attrLbl, ContainerType container) {
		addContentItem(attrId, attrLbl, ContentItemClassType.DOC, container);
	}
	
	private static void addContentItem(String attrId, String attrLbl, ContentItemClassType type, ContainerType container) {
		ContentItemType richTextItem = new ContentItemType();
		richTextItem.setId(attrId);
		richTextItem.setLabel(attrLbl);
		richTextItem.setType(type);
		
		container.getContentItem().add(richTextItem);
	}
	
	private static ContainerType removeContainerFromList(String containerQId, List<ContainerType> list) {
		
		ContainerType removedCntnr = null;
		
		for (Iterator<ContainerType> itr = list.iterator(); itr.hasNext(); ) {
			
			ContainerType cont = itr.next();
			
			if (cont.getQualifiedId().equals(containerQId)) {

				removedCntnr = cont;
				itr.remove();
				
			} else {
				removedCntnr = removeContainerFromList(containerQId, cont.getContainer());
				
			}
			
			if (removedCntnr != null) {
				break;
			}
		}
		
		return removedCntnr;
	}
	
	public static ContainerType removeContainer(ContentTemplate template, String containerQId) {
		ContainerType container = removeContainerFromList(containerQId, template.getDataDefinition().getContainer());
		removeContainerReferences(template, container);
		return container;
	}

	private static void removeContainerReferences(ContentTemplate template, ContainerType container) {
		
		if (container != null) {

			String containerQId = container.getQualifiedId();
			
			TemplateContainerWalker templateWalker = new TemplateContainerWalker(template, 
					(cnt) -> !TemplateUtil.isLinkedContainer(cnt));
			
			templateWalker.walk((visitContainer) -> {

				// remove where this container is a linked container
				for (Iterator<ContainerType> itr = visitContainer.getContainer().iterator(); itr.hasNext(); ) {
					
					ContainerType subContainer = itr.next();
					
					if (TemplateUtil.isLinkedContainer(subContainer)
							&& subContainer.getLinkContainerQId().equals(container.getQualifiedId())) {
						itr.remove();
					}
				}
				
				// remove the content item where this container is the ref-list store
				for (Iterator<ContentItemType> itr = visitContainer.getContentItem().iterator(); itr.hasNext(); ) {

					ContentItemType ci = itr.next();
					
					if (ci.getBounded() != null && ci.getBounded().getRefList() != null
							&& ci.getBounded().getRefList().getDatastore() != null
							&& ci.getBounded().getRefList().getDatastore().getStoreId().equals(container.getQualifiedId())) {
						itr.remove();
					}
				}
				
			});

			// remove UI Definition
			for (Iterator<ContentUIDefType> itr = template.getUiDefinition().getContentUIDef().iterator(); itr.hasNext(); ) {
				
				ContentUIDefType uiDef = itr.next();
				
				if (uiDef.getQualifiedId().equals(containerQId)
						|| uiDef.getQualifiedId().startsWith(containerQId + ".")) {
					itr.remove();
				}
			}
			
			// remove sub-container references
			for (ContainerType subCntnr : container.getContainer()) {
				if (!TemplateUtil.isLinkedContainer(subCntnr)) {
					removeContainerReferences(template, subCntnr);
				}
			}
		}
	}

	public static void updateContainerOrder(ContentTemplate template, ContainerOrder order) {
		
		Map<String, Integer> orderMap = order.getContainerOrder();
		
		if (CollectionUtil.isNotEmpty(orderMap)) {
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template);
			
			walker.walk((cont) -> {
				Integer displayOrder = orderMap.get(cont.getQualifiedId());
				if (displayOrder != null) {
					cont.setDisplayOrder(BigInteger.valueOf(displayOrder));
				}
			});
		}
	}

	public static void initContainerOrder(ContentTemplate template) {
		
		TemplateContainerWalker walker = new TemplateContainerWalker(template);
		
		walker.walk(new ContainerVisitor() {
			
			private int order = 0;
			
			@Override
			public void visit(ContainerType container) {
				container.setDisplayOrder(BigInteger.valueOf(++order));
			}
			
		});
	}

	public static String getNextBizDimColor(Set<String> usedColors) {
		for (String color : BIZ_DIM_COLORS) {
			if (!usedColors.contains(color)) {
				return color;
			}
		}
		return null;
	}
	
}
