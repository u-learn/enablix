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
	return obj.constructor === Array;
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
