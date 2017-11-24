import { ContentItem } from './content-item.model';
import { QualityControlledContent } from './quality-controlled-content.model';

export class BaseContainer extends QualityControlledContent {
  contentItem: ContentItem[]
}