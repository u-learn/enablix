import { BaseContainer } from './base-container.model';

export class Container extends BaseContainer {
  
  container: Container[];
  referenceable: boolean;
  refData: boolean;
  single: boolean;
  linkContainerQId: string;
  linkContentItemId: string;
  businessCategory: string;

  // values added on UI
  color: string;
  titleItemId: string;
  textItemId: string;
}