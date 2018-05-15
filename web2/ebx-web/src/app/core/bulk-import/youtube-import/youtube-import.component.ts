import { Component, OnInit, ViewEncapsulation, Output, EventEmitter } from '@angular/core';

import { GoogleDriveService } from '../google-drive.service';
import { ImportRecord, ImportRequest } from '../bulk-import.model';
import { AlertService } from '../../alert/alert.service';

declare var google: any;

@Component({
  selector: 'ebx-youtube-import',
  templateUrl: './youtube-import.component.html',
  styleUrls: ['./youtube-import.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class YoutubeImportComponent implements OnInit {

  @Output() onFileSelection = new EventEmitter<void>();
  @Output() onImportRequest = new EventEmitter<ImportRequest>();

  constructor(private gdrive: GoogleDriveService,
    private alert: AlertService) { }

  ngOnInit() {
    this.gdrive.loadLibraries();
  }

  createFinder() {
    this.gdrive.createYoutubePicker(this.pickerCallback.bind(this));
  }

    // A simple callback implementation.
  pickerCallback(data: any) {
    
    console.log(data);
    
    if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {  

      this.onFileSelection.emit();

      let videoList: ImportRecord[] = [];
      data[google.picker.Response.DOCUMENTS].forEach(rec => videoList.push(new YoutubeVideo(rec)));

      var request: ImportRequest = {
        source: 'YOUTUBE',
        sourceDetails: {},
        records: videoList
      }

      this.onImportRequest.emit(request);
    }
  }

}

export class YoutubeVideo extends ImportRecord {

  constructor(ytVideo: any) {
    
    super();
    
    this.id = ytVideo.id;
    this.sourceRecord = ytVideo;
    this.title = ytVideo.name;
    this.thumbnailUrl = null;

    if (ytVideo.thumbnails && ytVideo.thumbnails[0]) {
      this.thumbnailUrl = ytVideo.thumbnails[0].url;
    }
  }

}
