export class UserTask {
  identity: string;
  code: string;
  name: string;
  status: string;
  steps: UserTaskStep[];
  details: any;

  // ui properties
  focusStep: UserTaskStep;
}

export class UserTaskStep {
  stepId: string;
  name: string;
  desc: string;
  status: string;
  milestonesCovered: string[];
  finalMilestone: string;
  details: any;

  // ui properties
  focus: boolean;
}