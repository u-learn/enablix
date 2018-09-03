
export class SalesforceCtx {
  
  static initialized: boolean;

  static linkedMappedContentRequest: any;

  static init(ctx: any) {
    
    if (ctx && ctx.refContentQId && ctx.refMatchAttrId && ctx.refMatchAttrValue
          && ctx.mapContentQId && ctx.mapMatchAttrId && ctx.mapMatchAttrValue) {
      
      SalesforceCtx.linkedMappedContentRequest = {
        refContentQId: ctx.refContentQId,
        refMatchAttrId: ctx.refMatchAttrId,
        refMatchAttrValue: ctx.refMatchAttrValue,
        mapContentQId: ctx.mapContentQId,
        mapMatchAttrId: ctx.mapMatchAttrId,
        mapMatchAttrValue: ctx.mapMatchAttrValue,
        contentBusinessCategory: 'BUSINESS_CONTENT'
      };
    }

    SalesforceCtx.initialized = true;
  }

  static isInitialized() {
    return SalesforceCtx.initialized;
  }

  static reset() {
    SalesforceCtx.linkedMappedContentRequest = null;
    SalesforceCtx.initialized = false;
  }

  static getLinkedMappedContentRequest() {
    return SalesforceCtx.linkedMappedContentRequest;
  }

}