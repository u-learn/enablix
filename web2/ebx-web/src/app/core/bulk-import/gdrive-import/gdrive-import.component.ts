import { Component, OnInit, ViewEncapsulation, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { forkJoin } from "rxjs/observable/forkJoin";

import { GoogleDriveService } from '../google-drive.service';
import { ImportRecord, ImportRequest } from '../bulk-import.model';
import { AlertService } from '../../alert/alert.service';

declare var google: any;

@Component({
  selector: 'ebx-gdrive-import',
  templateUrl: './gdrive-import.component.html',
  styleUrls: ['./gdrive-import.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GdriveImportComponent implements OnInit {

  @Output() onFileSelection = new EventEmitter<void>();
  @Output() onImportRequest = new EventEmitter<ImportRequest>();

  constructor(private gdrive: GoogleDriveService,
    private alert: AlertService) { }

  ngOnInit() {
    this.gdrive.loadLibraries();
  }

  createPicker() {
    this.gdrive.authAndCreatePicker(this.pickerCallback.bind(this));
  }

  // A simple callback implementation.
  pickerCallback(data: any) {
  
    if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {  

      this.onFileSelection.emit();

      let fileList: ImportRecord[] = [];
      let folderObs: Observable<ImportRecord[]>[] = [];

      data[google.picker.Response.DOCUMENTS].forEach(
        (doc: any) => {
          
          console.log(doc);
          
          if (doc.type === 'folder') {
          
            console.log("******** Folder: ");
          
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
                "auth_code": this.gdrive.getAuthCode()
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
