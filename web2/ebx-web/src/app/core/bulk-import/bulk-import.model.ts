import { SelectOption } from '../select/select.component';

export class ImportRecord {
  
  id: string;
  title: string;
  contentQId: string;
  sourceRecord: any;
  tags: any = {};

  contentType: SelectOption[];
  thumbnailUrl: string;

}

export class ImportRequest {

  source: string;
  sourceDetails: any;
  records: ImportRecord[];

}