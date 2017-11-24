import { BaseDocumentEntity } from './base-document-entity.model';

export class Tenant extends BaseDocumentEntity {
  tenantId: string;
  name: string;
  defaultTemplateId: string;
}