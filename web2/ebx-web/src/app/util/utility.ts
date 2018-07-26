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

  static getElementId(qId: string) : string {
    var elemId = qId;
    var lastIndx = qId.lastIndexOf(".");
    if (lastIndx > 0) {
      elemId = qId.substr(lastIndx + 1);
    }
    return elemId;
  }

  static getQId(parentQId: string, elemId: string) {
    return parentQId + "." + elemId;
  }

  static isNullOrUndefined(obj) {
    return obj == null || obj == undefined;
  }

  static readSearchFiltersInQueryParams(queryParams: any) {
    
    var filters = null;
    
    for (let param in  queryParams) {
      if (param.startsWith("sf_")) {
        if (!filters) filters = {};
        filters[param.substr(3)] = queryParams[param];
      }
    }

    return filters;
  }

  static removeFileExtn(filename: string) {
    var response = filename;
    if (filename) {
      var n = filename.lastIndexOf('.');
      if (n > 0) {
        response = filename.substr(0, n);
      }
    }
    return response;
  }

  static openPopupInCenter(url, title, w, h) {
    // Fixes dual-screen position                         Most browsers      Firefox
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;
    var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    try {
      newWindow.focus();   
    } catch (e) {
      alert("Pop-up Blocker is enabled! Please enable pop-up for this site.");
    } 

    return newWindow;
  }

  static syncPostAjaxCall(url: string, data: any, callback: (responseText: string) => void) {
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        callback(JSON.parse(this.responseText));
      }
    };

    xhttp.open("POST", url, false);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(JSON.stringify(data));
  }

}