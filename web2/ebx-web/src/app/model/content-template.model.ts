import { Container } from './container.model';
import { UIDefinition } from './ui-definition.model';
import { PortalUIDefinition } from './portal-ui-definition.model';
import { DataDefinition } from './data-definition.model';

export class ContentTemplate {
  
  id: string;
  version: string;
  
  dataDefinition: DataDefinition;
  uiDefinition: UIDefinition;
  portalUIDefinition: PortalUIDefinition;

}