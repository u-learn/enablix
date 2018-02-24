export class Utility {
  
  static copyToClipboard(text: string) {
    var aux = document.createElement("input");
    aux.setAttribute("value", text);
    document.body.appendChild(aux);
    aux.select();
    document.execCommand("copy");
    document.body.removeChild(aux);
  }

  static removeNullProperties(obj) {
  
    var removeProps = [];
    
    for (var key in obj) {
      let value = obj[key];
      if (!value) {
        removeProps.push(key);
      }
    }
    
    removeProps.forEach((prop) => {
      delete obj[prop];
    });
  }

  static arrayContains(arr: any[], item: any, matchFn?: (a, b) => boolean) : boolean {
    var i = arr.length;
    while (i--) {
      if (matchFn ? matchFn(arr[i], item) : arr[i] === item) {
          return true;
      }
    }
    return false;
  }

  static sortArrayByLabel(arr: any[]) {
    if (arr) {
      arr.sort((a,b) => { return a.label == b.label? 0 : (a.label < b.label ? -1 : 1); });
    }
  }

  static getParentQId(qId: string) : string {
    var parentQId = null;
    var lastIndx = qId.lastIndexOf(".");
    if (lastIndx > 0) {
      parentQId = qId.substr(0, lastIndx);
    }
    return parentQId;
  }

}