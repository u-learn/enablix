import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

import { UserService } from './user.service';

@Directive({
  selector: '[ebxAccess]'
})
export class AccessDirective {

  hasView: boolean = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private user: UserService) { }

  @Input() set ebxAccess(perm: string) {
    
    let hasPerm = this.user.userHasPermission(perm);

    if (hasPerm && !this.hasView) {
      
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.hasView = true;

    } else if (!hasPerm && this.hasView) {
      this.viewContainer.clear();
      this.hasView = false;
    }

  }

}
