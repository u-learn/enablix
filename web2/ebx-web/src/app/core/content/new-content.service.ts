import { Injectable } from '@angular/core';

import { ContentTemplateService } from '../content-template.service';
import { NewContent } from '../../model/new-content.model';
import { UrlCaptureInfo, FileUploadInfo, TextInfo, AssetInfo } from '../../model/upload-info.model';
import { EmbedInfoService } from '../upload/embed-info.service';
import { EmbedInfo } from '../../model/embed-info.model';
import { ContentItem } from '../../model/content-item.model';

@Injectable()
export class NewContentService {

  content: NewContent;

  constructor(private contentTemplateService: ContentTemplateService,
              private embedInfoService: EmbedInfoService) { }

  getNewContent() : NewContent {
    return this.content;
  }

  captureUrlContent(urlInfo: UrlCaptureInfo, embedInfo: EmbedInfo) : any {
    
    const container = this.contentTemplateService.getContainerByQId(urlInfo.containerQId);
    
    if (container) {

      this.content = {
        container: container,
        data: {
          '__title': urlInfo.title,
          '__decoration': {}
        },
        urlInfo: urlInfo
      }

      let titleFldId = container.titleItemId;

      let urlFldId = container.urlItemId;

      /*for (let i = 0; i < container.contentItem.length; i++) {
      
        let item: ContentItem = container.contentItem[i];
        
        if (item.type == 'TEXT') {
          
          if (!titleFldId) {
            
            titleFldId = item.id;

          } else if (!urlFldId && item.id != titleFldId) {
            
            urlFldId = item.id;
            break;
          }
        }
      }*/

      if (titleFldId) {
        this.content.data[titleFldId] = urlInfo.title;
      }

      if (urlFldId) {
        this.content.data[urlFldId] = urlInfo.url;
      }
    }

    const embeddedUrl = embedInfo ? this.embedInfoService.toEmbeddedUrl(embedInfo) :
                          this.embedInfoService.defaultEmbeddedUrl(urlInfo);

    if (embeddedUrl) {
      this.content.data['__urls'] = [embeddedUrl];
    }

    return this.content;
  }

  captureTextContent(textInfo: TextInfo) : any {
    
    const container = this.contentTemplateService.getContainerByQId(textInfo.containerQId);
    
    if (container) {

      this.content = {
        container: container,
        data: {
          '__title': textInfo.title,
          '__decoration': {}
        },
        textInfo: textInfo
      }

      let titleFldId = container.titleItemId
      let descFldId = container.textItemId;

      if (titleFldId) {
        this.content.data[titleFldId] = textInfo.title;
      }

      if (descFldId) {
        this.content.data[descFldId] = textInfo.text;
        this.content.data.__decoration.__textContent = textInfo.text;
      }
    }

    return this.content;
  }

  captureAsset(assetInfo: AssetInfo) {
    const container = this.contentTemplateService.getContainerByQId(assetInfo.containerQId);
    
    if (container) {

      this.content = {
        container: container,
        data: {
          '__title': assetInfo.title,
          '__decoration': {}
        }
      }

      let titleFldId = container.titleItemId
      let descFldId = container.textItemId;

      if (titleFldId) {
        this.content.data[titleFldId] = assetInfo.title;
      }

    }

    return this.content;
  }


  captureFileContent(fileInfo: FileUploadInfo) {

    const container = this.contentTemplateService.getContainerByQId(fileInfo.containerQId);
    
    if (container) {

      this.content = {
        container: container,
        data: {
          '__title': fileInfo.title
        },
        fileInfo: fileInfo
      }

      let titleFldId = container.titleItemId;
      let docFldId = null;

      for (let i = 0; i < container.contentItem.length; i++) {
      
        let item: ContentItem = container.contentItem[i];
        
        if (item.type == 'TEXT') {
          if (!titleFldId) {
            titleFldId = item.id;
          }
        }

        if (item.type == 'DOC') {
          docFldId = item.id;
          fileInfo.docMetadata.contentQId = item.qualifiedId;
          break;
        }

      }

      if (titleFldId) {
        this.content.data[titleFldId] = fileInfo.title;
      }

      if (docFldId) {
        this.content.data[docFldId] = fileInfo.docMetadata;
      }
    }

    return this.content;
  }

  clear() {
    this.content = null;
  }

}
