export class FilterMetadata {
  
  field: string;
  operator: ConditionOperator = ConditionOperator.EQ;
  dataType: DataType = DataType.STRING;
  dateFilter?: DateFilterConfig;
}

export enum ConditionOperator {
  EQ = "EQ", 
  NOT_EQ = "NOT_EQ", 
  LT = "LT", 
  GT = "GT", 
  LTE = "LTE", 
  GTE = "GTE", 
  IN = "IN", 
  NOT_IN = "NOT_IN", 
  EXISTS = "EXISTS", 
  REGEX = "REGEX"
}

export enum DataType {
  STRING = "STRING",
  DATE = "DATE",
  BOOL = "BOOL",
  NUMBER = "NUMBER"
}

export class DateFilterConfig {
  valueType: ValueType;
}

export enum ValueType {
  LAST_X_DAYS = "LAST_X_DAYS"
}