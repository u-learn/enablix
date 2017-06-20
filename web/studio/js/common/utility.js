Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
};

Array.prototype.remove = function(obj) {
	var indx = this.indexOf(obj);
	if (indx != -1) {
		this.splice(indx, 1);
	}
}

var isNullOrUndefined = function(obj) {
	return obj === null || obj  === undefined;
};

var isNotNullOrUndefined = function(obj) {
	return obj !== null && obj !== undefined;
};


var isArray = function(obj) {
	return !isNullOrUndefined(obj) && obj.constructor === Array;
};

var isArrayAndEmpty = function(obj) {
	return  isArray(obj) && obj.length == 0;
};

var isString = function(obj) {
	return typeof obj === 'string';
}

var isStringAndEmpty = function(obj) {
	return isString(obj) && obj.trim().length == 0;
}

var removeNullProperties = function(obj) {
	
	var removeProps = [];
	
	angular.forEach(obj, function(value, key) {
		if (isNullOrUndefined(value)) {
			removeProps.push(key);
		}
	});
	
	angular.forEach(removeProps, function(prop) {
		delete obj[prop];
	});
}

var removeNullOrEmptyProperties = function(obj) {
	
	var removeProps = [];
	
	angular.forEach(obj, function(value, key) {
		if (isNullOrUndefined(value) || isArrayAndEmpty(value)) {
			removeProps.push(key);
		}
	});
	
	angular.forEach(removeProps, function(prop) {
		delete obj[prop];
	});
}

function linkify(text) {
    var urlRegex =/(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    return text.replace(urlRegex, function(url) {
        return '<a href="' + url + '">' + url + '</a>';
    });
};


function getUrlParameters($location) {
	return $location.search();
}


function chunkArray(arr, size) {
	
	size = arr.length < size ? arr.length : size;
	
	var newArr = [];
	for (var n = 0; n < size; n++) {
		newArr.push([]);
	}
	
	for (var i = 0; i < arr.length; i+=size) {
		for (var k = 0; k < size && (i+k) < arr.length; k++) {
			newArr[k].push(arr[i+k]);
		}
	}
	
	return newArr;
}

function subtractDaysFromDate(_date, _noOfDays) {
	
	var noOfDaysInMilliseconds = (24*60*60*1000) * _noOfDays;
	
	var newDate = new Date();
	newDate.setTime(_date.getTime() - noOfDaysInMilliseconds);
	
	return newDate;
}


function sortByLabelProp(c1, c2) {
	return c1.label == c2.label ? 0 : (c1.label < c2.label ? -1 : 1);
}

function isDateObject(_obj) {
	return typeof _obj.getMonth === 'function';
}

function isFunction(_obj) {
	return typeof _obj === 'function';
}

var ALPHABET = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
var RANDOM_ID_LENGTH = 4;
var generateUIDRandom = function() {
	var rtn = '';
	for (var i = 0; i < RANDOM_ID_LENGTH; i++) {
		rtn += ALPHABET.charAt(Math.floor(Math.random() * ALPHABET.length));	
	}
	return rtn;
}

function generateUID() {
	return generateUIDRandom() + "" + new Date().getTime();
}

function transformArrayToDoubleLinkedList(arr) {

	for (var i = 0; i < arr.length; i++) {
	
		var arrItem = arr[i];

		if (i != 0) {
			// if not first node, set previous node
			arrItem.previousNode = arr[i-1];
			
		} else {
			arrItem.previousNode = null;
		}
		
		if (i < arr.length - 1) {
			// if not last node, set next node
			arrItem.nextNode = arr[i+1];
			
		} else {
			arrItem.nextNode = null;
		}
		
	}
}