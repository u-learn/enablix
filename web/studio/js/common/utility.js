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