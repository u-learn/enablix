import { Direction } from '../core/model/pagination.model';

export class DataPage {
  content: any[];
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  sort: PageSort;
  totalElements: number;
  totalPages: number;
}

export class PageSort {
  direction: Direction;
  property: string;
  ignoreCase: boolean;
  ascending: boolean;
}