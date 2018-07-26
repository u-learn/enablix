import { TemplateContainerWalker } from '../util/template-container-walker';
import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { ContentTemplate } from '../model/content-template.model';
import { Constants } from '../util/constants';
import { Utility } from '../util/utility';

export class ContentTemplateCache {

  contentTemplate: ContentTemplate;

  qIdToContainerMap = {};
  concreteContainers: Container[] = [];
  bizContentContainers: Container[] = [];
  bizDimensionContainers: Container[] = [];

  contentStackQIdMap = {};

  autoPopulateDependents: any = {};

  private index = 0;
  colors = [];

  constructor(contentTemplate: ContentTemplate) {
    this.contentTemplate = contentTemplate;
    this.initContainerColors();
    this.init();
  }

  private init() {
    let walker = new TemplateContainerWalker(this.contentTemplate.dataDefinition.container);
    walker.walk(container => {
      
      this.qIdToContainerMap[container.qualifiedId] = container;
      if (!container.singularLabel) {
        container.singularLabel = container.label;
      }

      if (container.refData && !container.color) {
        container.color = Constants.refDataColor;
      }
      
      if (!this.isLinkedContainer(container)) {
        
        container.titleItemId = this.getContainerLabelAttrId(container.qualifiedId);
        this.checkAndSetContentTypeItemIds(container);
        this.concreteContainers.push(container);

        if (this.isBusinessContent(container)) {
          
          this.bizContentContainers.push(container);
          if (!container.color) {
            container.color = Constants.bizContentColor;
          }

        } else if (this.isBusinessDimension(container)) {
          this.bizDimensionContainers.push(container);
          if (!container.color) {
            console.log("Getting color: " + container.qualifiedId + ", index: " + this.index);
            container.color = this.getContainerColor(this.index++);
          }
        }

        

        if (!container.color) {
          container.color = Constants.defaultTypeColor;
        }

      }

    });

    // populate linked BIZ_CONTENT containers
    for (let key in this.qIdToContainerMap) {
      var cntr = this.qIdToContainerMap[key];
      var lnkBizConts = this.getLinkedBizContentContainers(cntr);
      if (lnkBizConts && lnkBizConts.length > 0) {
        cntr.linkedBizContent = lnkBizConts;
      }
    }

    this.bizDimensionContainers.sort(
      (a: Container, b: Container) => { return a.displayOrder - b.displayOrder; });

    this.initAutoPopulateDependents();
  }

  initAutoPopulateDependents() {
    
    var uiDefinitions = this.contentTemplate.uiDefinition.contentUIDef;
    
    // find the container UI definition
    for (var i = 0; i < uiDefinitions.length; i++) {

      var uiDef = uiDefinitions[i]; 
      
      if (uiDef.text && uiDef.text.autoPopulate) {
        
        var dependentQId = uiDef.qualifiedId;
        var refItemId = uiDef.text.autoPopulate.refContentItem.itemId;
        
        var parentQId = Utility.getParentQId(dependentQId);
        var dependentId = Utility.getElementId(dependentQId);
        var refItemQId = Utility.getQId(parentQId, refItemId);

        var dependents: string[] = this.autoPopulateDependents[refItemQId];
        if (!dependents) {
          dependents = [];
          this.autoPopulateDependents[refItemQId] = dependents;
        }

        dependents.push(dependentId);
      }
    }
  }

  getAutoPopulateDependentItemIds(refQId: string) {
    return this.autoPopulateDependents[refQId];
  }

  getContainerByQId(containerQId: string) : Container {
    return this.qIdToContainerMap[containerQId];
  }

  getContainerColor(containerIndex: number) : string {
    return this.colors[containerIndex];
  }

  isLinkedContainer(container: Container) : boolean {
    return container.linkContainerQId != null;
  }

  isBusinessDimension(container: Container) : boolean {
    return !container.refData && 'BUSINESS_DIMENSION' == container.businessCategory;
  }

  isBusinessContent(container: Container) : boolean {
    return 'BUSINESS_CONTENT' == container.businessCategory;
  }

  checkAndSetContentTypeItemIds(container: Container) {
    
    var descItemId = null;
    var textItemDef = null;
    
    for (let i = 0; i < container.contentItem.length; i++) {
    
      let contentItem = container.contentItem[i];
      if (contentItem.type == 'RICH_TEXT' && contentItem.id != container.titleItemId) {
        descItemId = contentItem.id;
        textItemDef = contentItem;
      } 

      if (contentItem.type == 'DOC') {
        container.docItemId = contentItem.id;
      }

      if (contentItem.type == 'CONTENT_STACK') {
        this.contentStackQIdMap[contentItem.qualifiedId] = contentItem;
        container.contentStackItemId = contentItem.id;
      }
    }

    container.textItemId = descItemId;
    container.textItemDef = textItemDef;
  }

  getContainerLabelAttrId(containerQId: string) : string {
        
    var cntnrUIDef = this.getUIDefinition(containerQId);
    
    var labelFieldId = null;
    
    if (cntnrUIDef) {
      
      var labelQId = cntnrUIDef.container.labelQualifiedId;
      
      if (!labelQId) {
        // check for portal heading config
        if (cntnrUIDef.container.portalConfig && cntnrUIDef.container.portalConfig.headingContentItem) {
          labelQId = cntnrUIDef.container.portalConfig.headingContentItem.id;
        }
      }

      if (labelQId) {
      
        if (labelQId.indexOf(containerQId) == 0) { // starts with container QId, strip it
          labelFieldId = labelQId.substring(containerQId.length + 1, labelQId.length);
          
        } else {
          labelFieldId = labelQId;
        }
      } 
    }
    
    return labelFieldId;
  }

  getUIDefinition(elementQId: string) {
        
    var elemUIDef = undefined;
    
    var uiDefinitions = this.contentTemplate.uiDefinition.contentUIDef;
    
    // find the container UI definition
    for (var i = 0; i < uiDefinitions.length; i++) {

      var uiDef = uiDefinitions[i]; 
      
      if (uiDef.qualifiedId == elementQId) {
        elemUIDef = uiDef;
        break;
      }
    }
    
    return elemUIDef;
  }

  getFreeInputContentItems(container: Container) : ContentItem[] {
    let freeInputItems = container.contentItem.filter(item => {
      return item.type == 'TEXT' || item.type == 'DATE_TIME' || item.type == 'NUMERIC' || item.type == 'RICH_TEXT'
              || (item.type == 'BOUNDED' && !this.isRelevanceItem(item));
    });
    return freeInputItems;
  }

  isRelevanceItem(item: ContentItem) {
    if (item.bounded) {
      var bndCont = this.getBoundedItemDatastoreContainer(item);
      if (bndCont) {
        return !bndCont.refData && this.isBusinessDimension(bndCont)
      }
    }
    return false;
  }

  getBoundedItemDatastoreContainer(contentItem: ContentItem) : Container {

    let container: Container = null;

    if (contentItem.bounded && contentItem.bounded.refList 
          && contentItem.bounded.refList.datastore) {
      let containerQId = contentItem.bounded.refList.datastore.storeId;
      container = this.getContainerByQId(containerQId);
    }

    return container;
  }

  getBoundedContentItemColor(contentItem: ContentItem) : string {
    let container = this.getBoundedItemDatastoreContainer(contentItem);
    return container ? container.color : Constants.defaultTypeColor;
  }

  getLinkedBizContentContainers(container: Container) {
    var linkedContainers = [];
    if (container.container) {
      container.container.forEach((lc) => {
        var lnkContainer = this.getContainerByQId(lc.linkContainerQId);
        if (lnkContainer && this.isBusinessContent(lnkContainer)) {
          linkedContainers.push(lc);
        }
      });
    }
    return linkedContainers;
  }

  /*

  generateRandomColor() : string {
    return '#' + (Math.random().toString(16) + "000000").substring(2,8);
  }

  initContainerColors() {
    let colors = new Set();
    for (let i = 0; i < 100; i++) {
      colors.add(this.getContainerColor(i));
    }
    console.log(colors);
  }

  */

  initContainerColors() {
    this.colors.push("#e75632");
    this.colors.push("#7147d3");
    this.colors.push("#4046ad");
    this.colors.push("#d0a914");
    this.colors.push("#a62078");
    this.colors.push("#70e58f");
    this.colors.push("#448e14");
    this.colors.push("#dbda1e");
    this.colors.push("#569cb1");
    this.colors.push("#6a1c9e");
    this.colors.push("#e0129f");
    this.colors.push("#c87b89");
    this.colors.push("#8fcc92");
    this.colors.push("#75684c");
    this.colors.push("#c81dfa");
    this.colors.push("#857610");
    this.colors.push("#d4b577");
    this.colors.push("#9c789b");
    this.colors.push("#ee7aaa");
    this.colors.push("#e6bcdc");
    this.colors.push("#bda2e6");
    this.colors.push("#6a905e");
    this.colors.push("#c23c0b");
    this.colors.push("#55ae52");
    this.colors.push("#b6410a");
    this.colors.push("#82514a");
    this.colors.push("#66818b");
    this.colors.push("#4968a3");
    this.colors.push("#be203f");
    this.colors.push("#0ee4b2");
    this.colors.push("#058bea");
    this.colors.push("#e6979d");
    this.colors.push("#d27862");
    this.colors.push("#18536c");
    this.colors.push("#968283");
    this.colors.push("#bdd0c0");
    this.colors.push("#179ac5");
    this.colors.push("#d9004f");
    this.colors.push("#befb21");
    this.colors.push("#c39989");
  }

}
