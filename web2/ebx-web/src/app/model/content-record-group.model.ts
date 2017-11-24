import { DataPage } from './data-page.model';
import { Container } from './container.model';

export class ContentRecordGroup {
  contentQId: string;
  records: DataPage;

  // added on UI
  container: Container;
}