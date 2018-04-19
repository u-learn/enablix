import { QualityControlledContent } from './quality-controlled-content.model';
import { BaseContent } from './base-content.model';

export class ContentItem extends QualityControlledContent {
  type: string;
  bounded: Bounded;
}

export class Bounded {
  fixedList?: BoundedFixedList;
  refList?: BoundedRefList;
  multivalued: boolean;
}

export class BoundedFixedList {
  data: FixedListData[];
}

export class FixedListData extends BaseContent {

}

export class BoundedRefList {
  datastore: BoundedListDatastore;
}

export class BoundedListDatastore {
  storeId: string;
  dataId: string;
  dataLabel: string;
  hyperlink?: boolean;
  location?: string;
}

