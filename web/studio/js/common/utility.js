Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
};


var isNullOrUndefined = function(obj) {
	return obj === null || obj  === undefined;
};

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