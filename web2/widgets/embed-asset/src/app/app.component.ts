import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styles: []
})
export class AppComponent implements OnInit {
  
  @ViewChild("downloadLink") downloadLink; 

  supportedExts: string[] = [
    "doc", "docx", "gdoc", "gsheet", "gslides", "pdf", "pps", 
    "ppt", "pptx", "rtf", "txt", "xls", "xlsx", "xml"
  ];

  record: any;
  type: string;

  urlType: string;
  iframeNotSupported: boolean;
  urlThumbnail: string;
  urlEmbedHtml: string;

  slides: string[];
  activeSlide: string;

  constructor(private http: HttpClient) {

  }

  ngOnInit() {
    
    var urlString = window.location.href;
    var url = new URL(urlString);
    var accessId = url.searchParams.get('id');

    if (accessId) {
      var apiUrl = environment.baseAPIUrl + '/ushare/' + accessId;
      this.http.get(apiUrl).subscribe(
        (res: any) => {
          console.log(res);
          this.record = res;
          this.init();
        },
        (err) => {
          alert("Error fetch content. Please try later.");
        }
      );
    }
  }

  init() {
    
    if (this.record) {
      
      if (this.record.preview) {
      
        this.type = this.record.preview.previewType;
        
        if (this.record.preview.embedInfo) {
          this.initUrlType(this.record.preview.embedInfo);
          this.iframeNotSupported = !this.record.preview.embedInfo.iframeEmbeddable;
        }

        if (this.record.preview.imageUrls) {
          this.initDocType();
        }
      }
    }
  }

  initUrlType(embedInfo : any) {
    
    this.urlType = embedInfo.oembed ? embedInfo.oembed.type : embedInfo.type;
    this.iframeNotSupported = !this.record.preview.embedInfo.iframeEmbeddable;

    if (this.urlType != 'rich' && this.urlType != 'link' 
        && this.record.preview.embedInfo.oembed && this.record.preview.embedInfo.oembed.html) {
      
      this.urlEmbedHtml = this.record.preview.embedInfo.oembed.html;

    } else {
      
      if (embedInfo.images && embedInfo.images[0]) {
        this.urlThumbnail = embedInfo.images[0].url;
      } else if (embedInfo.oembed) {
        this.urlThumbnail = embedInfo.oembed.thumbnailUrl;
      }
    }

  }

  initDocType() {
    
    this.slides = [];

    if (this.record.preview.imageUrls) {
      this.slides = this.record.preview.imageUrls;
      this.activeSlide = this.slides[0];
    }
  }

  docDownload() {
    if (this.downloadLink) {
      this.downloadLink.nativeElement.click();
    }
  }

  openUrl() {

    if (this.record.hyperlink) {
      let url = this.record.hyperlink.href;
      window.open(url, '_blank');
    }

  }

  noPreviewImg() {
    let imgUrl = "/assets/images/icons/file.svg";
    if (this.record.preview && this.record.preview.docType 
        && this.supportedExts.includes(this.record.preview.docType)) {
      imgUrl = "/assets/images/icons/file_" + this.record.preview.docType + ".svg";
    }
    return imgUrl;
  }

}
