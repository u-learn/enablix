import { BaseDocumentEntity } from './base-document-entity.model';

export class Tenant extends BaseDocumentEntity {
  tenantId: string;
  name: string;
  defaultTemplateId: string;
}

export class TableAction {
  
  label: string;
  iconClass: string;

}

export interface TableActions<T> {
  
  getAvailableActions(selectedRecors: T[]) : TableAction[];

}

