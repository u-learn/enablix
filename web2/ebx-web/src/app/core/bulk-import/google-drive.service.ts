import { Injectable, NgZone } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs/Observable';
import { fromPromise } from 'rxjs/observable/fromPromise';

declare var gapi: any;
declare var google: any;

@Injectable()
export class GoogleDriveService {

  // The Client ID obtained from the Google API Console. Replace with your own Client ID.
  private clientId = environment.googleAPIClientId;

  // Scope to use to access user's drive.
  private scope = 'https://www.googleapis.com/auth/drive.readonly';

  private pickerApiLoaded: boolean = false;
  private auth2ApiLoaded: boolean = false;

  private oauthToken: string;
  private authCode: string;

  constructor(private ngZone: NgZone) { }

  loadLibraries() {
    if (!this.pickerApiLoaded || !this.auth2ApiLoaded) {
      this.ngZone.run(() => {
        gapi.load('auth2', this.onAuthApiLoad.bind(this));
        gapi.load('picker', this.onPickerApiLoad.bind(this));
        gapi.load('client', () => {
          gapi.client.load("drive", "v3");
        });
      });  
    }
  }

  onAuthApiLoad() {
    this.auth2ApiLoaded = true;
  }

  onPickerApiLoad() {
    this.pickerApiLoaded = true;
  }

  getAuthCode() : string {
    return this.authCode;
  }

  authAndCreatePicker(callback: any) {

    gapi.auth2.authorize(
      {
        client_id: this.clientId,
        scope: this.scope,
        response_type: 'permission code'
      }, 
      (authResult: any) => {
        if (authResult && !authResult.error) {
          this.oauthToken = authResult.access_token;
          this.authCode = authResult.code;
          this.createPicker(callback, this.oauthToken);
        }
      }
    );

  }

  // Create and render a Picker object for picking user Photos.
  createPicker(callback: any, authToken: string) {

    if (this.pickerApiLoaded && this.auth2ApiLoaded) {
      
      if (authToken) {
      
        var docView = new google.picker.​DocsView();
        docView.setIncludeFolders(true);
        docView.setSelectFolderEnabled(true);

        var picker = new google.picker.PickerBuilder().
            addView(docView).
            setOAuthToken(this.oauthToken).
            enableFeature(google.picker.Feature.MULTISELECT_ENABLED).
            setCallback(callback).
            build();
        picker.setVisible(true);

      }
    }
  }

  getFilesInFolder(folderId: string) : Observable<any> {
    
    var q = "'" + folderId + "' in parents and mimeType != 'application/vnd.google-apps.folder'";
    
    var flPromise = gapi.client.drive.files.list({
        oauth_token: this.oauthToken,
        q: q//,
        //fields: "files(id,name,mimeType,parents,iconLink)"
      }).then(
        (files: gapi.client.Response<gapi.client.drive.FileList>) => {
          return files.result.files;
        },
        (err) => {
          return err;
        }
      );

    return fromPromise(flPromise);
  }

}
