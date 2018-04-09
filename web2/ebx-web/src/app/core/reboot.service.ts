import { Injectable, NgZone } from '@angular/core';

import { BootController } from '../../boot-control';

@Injectable()
export class RebootService {

  constructor(private ngZone: NgZone) { }

  reboot() {
    console.log("Rebooting...");
    // Triggers the reboot in main.ts        
    this.ngZone.runOutsideAngular(() => BootController.getbootControl().restart());
  }

}
