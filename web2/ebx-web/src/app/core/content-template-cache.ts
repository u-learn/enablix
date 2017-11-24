import { TemplateContainerWalker } from '../util/template-container-walker';
import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { ContentTemplate } from '../model/content-template.model';
import { Constants } from '../util/constants';

export class ContentTemplateCache {

  contentTemplate: ContentTemplate;

  qIdToContainerMap = {};
  concreteContainers: Container[] = [];
  bizContentContainers: Container[] = [];
  bizDimensionContainers: Container[] = [];

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
      
      if (!this.isLinkedContainer(container)) {
        
        
        container.titleItemId = this.getContainerLabelAttrId(container.qualifiedId);
        container.textItemId = this.checkAndGetTextAttrId(container);
        this.concreteContainers.push(container);

        if (this.isBusinessContent(container)) {
          
          this.bizContentContainers.push(container);
          container.color = Constants.bizContentColor;

        } else if (this.isBusinessDimension(container)) {
          this.bizDimensionContainers.push(container);
          container.color = this.getContainerColor(this.index++);
        }

      }

    });
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
    return 'BUSINESS_DIMENSION' == container.businessCategory;
  }

  isBusinessContent(container: Container) : boolean {
    return 'BUSINESS_CONTENT' == container.businessCategory;
  }

  checkAndGetTextAttrId(container: Container) {
    
    var descItemId = null;
    
    for (let i = 0; i < container.contentItem.length; i++) {
    
      let contentItem = container.contentItem[i];
      if (contentItem.type == 'RICH_TEXT' && contentItem.id != container.titleItemId) {
        descItemId = contentItem.id;
      } 

      if (contentItem.type == 'DOC') {
        // this is a doc item type, nullify item id 
        descItemId = null;
        break;
      }
    }

    return descItemId;
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
      return item.type == 'TEXT' || item.type == 'DATE_TIME' || item.type == 'NUMERIC';
    });
    return freeInputItems;
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
    this.colors.push("#1e0bbb");
    this.colors.push("#549d19");
    this.colors.push("#4cca7f");
    this.colors.push("#b81b8d");
    this.colors.push("#a485b7");
    this.colors.push("#907ccc");
    this.colors.push("#7504ec");
    this.colors.push("#f7c225");
    this.colors.push("#29e59e");
    this.colors.push("#9fb67e");
    this.colors.push("#c834a6");
    this.colors.push("#d8439e");
    this.colors.push("#e680b1");
    this.colors.push("#2550f2");
    this.colors.push("#ae6893");
    this.colors.push("#dbda1e");
    this.colors.push("#52c07f");
    this.colors.push("#67a821");
    this.colors.push("#df491f");
    this.colors.push("#b03159");
    this.colors.push("#14a176");
    this.colors.push("#569cb1");
    this.colors.push("#4a8ed8");
    this.colors.push("#57d658");
    this.colors.push("#6754f4");
    this.colors.push("#6a1c9e");
    this.colors.push("#e0129f");
    this.colors.push("#7b53d5");
    this.colors.push("#c87b89");
    this.colors.push("#8fcc92");
    this.colors.push("#75684c");
    this.colors.push("#c81dfa");
    this.colors.push("#857610");
    this.colors.push("#344286");
    this.colors.push("#d4b577");
    this.colors.push("#9c789b");
    this.colors.push("#936e94");
    this.colors.push("#ee7aaa");
    this.colors.push("#17762e");
    this.colors.push("#6a2c70");
    this.colors.push("#435fb5");
    this.colors.push("#ed5a99");
    this.colors.push("#e6bcdc");
    this.colors.push("#9909b7");
    this.colors.push("#bda2e6");
    this.colors.push("#6a905e");
    this.colors.push("#866422");
    this.colors.push("#eacab6");
    this.colors.push("#26acb6");
    this.colors.push("#7d2ce8");
    this.colors.push("#7709cd");
    this.colors.push("#b0ace8");
    this.colors.push("#c9b38f");
    this.colors.push("#ba6d77");
    this.colors.push("#c23c0b");
    this.colors.push("#55ae52");
    this.colors.push("#b6410a");
    this.colors.push("#f42113");
    this.colors.push("#0435f6");
    this.colors.push("#3ffa69");
    this.colors.push("#798328");
    this.colors.push("#faddda");
    this.colors.push("#35f32b");
    this.colors.push("#82514a");
    this.colors.push("#4fa45f");
    this.colors.push("#14b569");
    this.colors.push("#be203f");
    this.colors.push("#5805bc");
    this.colors.push("#058bea");
    this.colors.push("#e6979d");
    this.colors.push("#b972db");
    this.colors.push("#d27862");
    this.colors.push("#18536c");
    this.colors.push("#968283");
    this.colors.push("#28bb28");
    this.colors.push("#94c88c");
    this.colors.push("#1dc683");
    this.colors.push("#3d90ff");
    this.colors.push("#dfd677");
    this.colors.push("#4ba2e9");
    this.colors.push("#e133d2");
    this.colors.push("#bdd0c0");
    this.colors.push("#bfe63c");
    this.colors.push("#bbe603");
    this.colors.push("#179ac5");
    this.colors.push("#553191");
    this.colors.push("#9f2bf8");
    this.colors.push("#d9004f");
    this.colors.push("#befb21");
    this.colors.push("#c39989");
    this.colors.push("#78e348");
    this.colors.push("#f705c1");
    this.colors.push("#89190b");
    this.colors.push("#ef5a16");
  }

}
