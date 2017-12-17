import { BaseDocumentEntity } from './base-document-entity.model';

export class UserSystemProfile {
  sendWeeklyDigest: boolean;
  defaultClientId: string;
  dataSegment: any;
  roles: any[];
}

export class UserBusinessProfile {
  orgName: string;
  attributes: {[key: string] : any};
}

export class UserProfile extends BaseDocumentEntity {
  name: string;
  email: string;
  userIdentity: string;
  systemProfile: UserSystemProfile;
  businessProfile: UserBusinessProfile;
}