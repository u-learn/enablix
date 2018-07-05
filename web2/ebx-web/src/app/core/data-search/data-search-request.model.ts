import { FilterMetadata } from './filter-metadata.model';
import { Pagination } from '../model/pagination.model';

export class DataSearchRequest {

  filters: { [key: string]: any };
  filterMetadata: { [key: string]: FilterMetadata};
  textQuery?: string;
  pagination: Pagination;
  projectedFields: string[];

}
