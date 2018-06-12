import { Component, OnInit, ViewEncapsulation, ViewChild, ElementRef, Output, EventEmitter, OnDestroy, Renderer2 } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { forkJoin } from "rxjs/observable/forkJoin";

import { GoogleDriveService } from '../google-drive.service';
import { ImportRecord, ImportRequest } from '../bulk-import.model';
import { AlertService } from '../../alert/alert.service';
import { environment } from '../../../../environments/environment';

declare var google: any;

@Component({
  selector: 'ebx-gdrive-import',
  templateUrl: './gdrive-import.component.html',
  styleUrls: ['./gdrive-import.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GdriveImportComponent implements OnInit, OnDestroy {

  @ViewChild('goauthFrame') goauthFrame: ElementRef;

  @Output() onFileSelection = new EventEmitter<void>();
  @Output() onImportRequest = new EventEmitter<ImportRequest>();

  stopListening: Function;

  defaultDomain: string = environment.domainUrl;
  goauthFrameSrc: string = environment.domainUrl + "/goauth2.html";

  constructor(private gdrive: GoogleDriveService,
    private alert: AlertService, private renderer: Renderer2) { }

  ngOnInit() {
    this.gdrive.loadLibraries();
    this.stopListening = this.renderer.listen('window', 'message', this.handleMessage.bind(this));
  }

  handleMessage(msg) {
    
    console.log("Window message...");
    console.log(msg);

    if (msg.origin == this.defaultDomain) {
      this.gdrive.createPickerUsingAuth(this.pickerCallback.bind(this), 
        msg.data.oauthToken, msg.data.authCode);
    }
  }

  createPicker() {
    var authConfig = this.gdrive.getAuthorizeConfig();
    this.goauthFrame.nativeElement.contentWindow.postMessage(authConfig, '*');
  }

  // A simple callback implementation.
  pickerCallback(data: any) {
  
    if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {  

      this.onFileSelection.emit();

      let fileList: ImportRecord[] = [];
      let folderObs: Observable<ImportRecord[]>[] = [];

      data[google.picker.Response.DOCUMENTS].forEach(
        (doc: any) => {
          
          if (doc.type === 'folder') {
          
            var obs$ = this.gdrive.getFilesInFolder(doc.id).map(
                (result: any) => {
                  let subFiles: ImportRecord[] = [];
                  if (result) {
                    result.forEach((fl) => {
                      subFiles.push(new GDriveFile(fl));
                    });
                  }
                  return subFiles;
                }
              );

            folderObs.push(obs$);

          } else {
            fileList.push(new GDriveFile(doc));
          }

        });

      folderObs.push(Observable.of(fileList));

      forkJoin(folderObs)
        .subscribe(
          (response) => {
            
            var merged = [].concat.apply([], response);
            
            var request: ImportRequest = {
              source: 'GOOGLEDRIVE',
              sourceDetails: {
                "auth_code": this.gdrive.getAuthCode(),
                "redirect_url": this.defaultDomain
              },
              records: merged
            }
            
            this.onImportRequest.emit(request);
          },
          (error) => {
            this.alert.error("Error loading file details. Please try again.", error.statusCode);
          }
        );

    }

  }

  ngOnDestroy() {
    console.log("Google Import destroyed");
    if (this.stopListening) {
      this.stopListening();
    }
  }

}

export class GDriveFile extends ImportRecord {

  constructor(gFile: any) {
    super();
    this.id = gFile.id;
    this.sourceRecord = gFile;
    this.title = gFile.name;
    this.thumbnailUrl = "https://drive.google.com/thumbnail?id=" + gFile.id;
  }

}
