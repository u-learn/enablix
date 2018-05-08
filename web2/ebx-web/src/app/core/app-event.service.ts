import { Injectable, EventEmitter, Output } from '@angular/core';

@Injectable()
export class AppEventService {

  @Output() onOpenAddAsset = new EventEmitter<void>();

  constructor() { }

  triggerOpenAddEvent() {
    this.onOpenAddAsset.emit();
  }

}
