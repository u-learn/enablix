export class Pagination {
  pageSize: number;
  pageNum: number;
  sort: SortCriteria;
}

export class SortCriteria {
  field: string;
  direction: Direction;
}

export enum Direction {
  ASC = "ASC",
  DESC = "DESC"
}