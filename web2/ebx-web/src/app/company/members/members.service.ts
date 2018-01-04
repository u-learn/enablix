import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../../core/api-url.service';
import { DataSearchService } from '../../core/data-search/data-search.service';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { FilterMetadata, DataType, ConditionOperator } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../model/pagination.model';
import { UserProfile } from '../../model/user.model';

@Injectable()
export class MembersService {

  static USER_PROFILE_DOMAIN = "com.enablix.core.domain.security.authorization.UserProfile";

  static filterMetadata : { [key: string] : FilterMetadata} = {
    "userRoleIn" : {
      "field" : "systemProfile.roles.$id",
      "operator" : ConditionOperator.IN,
      "dataType" : DataType.STRING
    }
  }

  constructor(private http:HttpClient, private urlService:ApiUrlService,
      private dsService: DataSearchService) { }

  getMembers(filters: {[key: string] : any}, pagination: Pagination) {
    
    let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = MembersService.filterMetadata;
    searchRequest.filters = filters;
    searchRequest.pagination = pagination;
    searchRequest.projectedFields = [];

    return this.dsService.getDataSearchResult(MembersService.USER_PROFILE_DOMAIN, searchRequest);
  }

  getAllSystemRoles() {
    let apiUrl = this.urlService.getAllSystemRolesUrl();
    return this.http.get(apiUrl);
  }

  checkUserExist(userId: string) {
    let apiUrl = this.urlService.postCheckUserExistUrl();
    return this.http.post(apiUrl, { username: userId });
  }

  addMember(userProfile: UserProfile) {
    let apiUrl = this.urlService.postAddMemberUrl();
    return this.http.post(apiUrl, userProfile);
  }

  updateMember(userProfile: UserProfile) {
    let apiUrl = this.urlService.postUpdateMemberUrl();
    return this.http.post(apiUrl, userProfile);
  }

  getMemberProfile(userIdentity: string) : Observable<UserProfile> {
    let apiUrl = this.urlService.getMemberProfileUrl(userIdentity);
    return this.http.get<UserProfile>(apiUrl);
  }

  deleteMembers(userProfileIdentities: string[]) : Observable<any> {
    let apiUrl = this.urlService.postDeleteMembersUrl();
    return this.http.post(apiUrl, { identities: userProfileIdentities });
  }

  getAllMembers() {
    let apiUrl = this.urlService.getAllMembersUrl();
    return this.http.get(apiUrl);
  }

}
