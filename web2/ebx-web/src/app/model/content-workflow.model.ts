export class SimpleActionInput {
  identity: string; // ref object identity
  notes: string;
}

export class ContentRequestDetail extends SimpleActionInput {
  contentQId: string;
  data: any;
  addRequest?: boolean = false;
}