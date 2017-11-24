import { BaseContent } from './base-content.model';
import { QualityConfig } from './quality-config.model';

export class QualityControlledContent extends BaseContent {
  qualityConfig: QualityConfig;
}