enablix.filters.filter('ebxFormatData', 
[		'$filter', '$compile',		
function($filter,   $compile) {
	
	return function(input, inputTypeDef, contentRecord, scope) {
		
		if (isNullOrUndefined(input)) { 
			return ""; 
		}
		
		var text = input;
		var dataType = inputTypeDef.type;
		
		switch(dataType) {
			
			case "NUMERIC": 
				text = input;
				break;
			
			case "DOC": 
				text = input.name;
				break;
				
			case "DATE_TIME":
				text = $filter('ebDate')(input);
				break;
		
			case "BOUNDED":
				if (input) {
				
					text = "";
					
					for (var i = 0; i < input.length; i++) {
						
						if (i != 0) {
							text += ", "
						}
					
						var item = input[i];
						var formattedItem = item.label;
						text += formattedItem;
					}
				}
				break;
				
			case "CONTENT_STACK":
				if (input) {
				
					text = "";
					
					for (var i = 0; i < input.length; i++) {
						
						if (i != 0) {
							text += "<br/>"
						}
					
						var item = input[i];
						var formattedItem = item.label + " [" + item.containerLabel + "]";
						text += item.containerLabel + " - " + item.label;
					}

				}
				break;
			
			case "RICH_TEXT":
				var richText = contentRecord[inputTypeDef.id + "_rt"];
				if (richText) {
					text = "<div class='ql-editor'>" + richText + "</div>";
				}
				break;
				
			case "TEXT":
								
				if (input) {
					
					var linkParams = {
						"cId" : contentRecord.identity,
						"cQId" : inputTypeDef.qualifiedId
					}
					
					text = $filter('ebxLinky')(input, "_blank", linkParams);
				}
				break;
		}
		
		return text;
	};
	
}]);

enablix.filters.filter('ebxLinky', ['$sanitize', '$location', '$state',
	function($sanitize, $location, $state) {
		//var LINKY_URL_REGEXP = /((ftp|https?):\/\/|(www\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>"”’]/, MAILTO_REGEXP = /^mailto:/;
		var LINKY_URL_REGEXP = /((ftp|https?):\/\/|(www\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>"”’]/, MAILTO_REGEXP = /^mailto:/;
	
		return function(text, target, linkParams) {
			
			if (!text)
				return text;
			
			function addText(text) {
				if (!text) {
					return;
				}
				html.push($sanitize(text));
			}
	
			function addLink(_url, text, linkParams) {
	
				var url = encodeURIComponent(_url);
				url = $location.protocol() + "://"
						+ $location.host() + ":"
						+ $location.port() + "/xlink?u="
						+ url;
	
				if (linkParams) {
					angular.forEach(linkParams, function(value, key) {
						url += "&" + key + "=" + value
					});
				}
	
				if ($state.includes("portal")) {
					url += "&atChannel=WEB";
				}
	
				html.push('<a ');
				if (angular.isDefined(target)) {
					html.push('target="', target, '" ');
				}
				html.push('href="', url.replace(/"/g, '&quot;'), '">');
				addText(_url.startsWith("mailto:") ? text : "Click Here");
				html.push('</a>');
			}
			
			var match;
			var raw = text;
			var html = [];
			var url;
			var i;
			
			while ((match = raw.match(LINKY_URL_REGEXP))) {
				
				// We can not end in these as they are
				// sometimes found at the end of the sentence
				url = match[0];
				
				// if we did not match ftp/http/www/mailto then assume mailto
				if (!match[2] && !match[4]) {
					url = (match[3] ? 'http://' : 'mailto:') + url;
				}
				
				i = match.index;
				addText(raw.substr(0, i));
				addLink(url, match[0].replace(MAILTO_REGEXP, ''), linkParams);
				raw = raw.substring(i + match[0].length);
			}
			addText(raw);
			return $sanitize(html.join(''));
	
			
		};
	} ]);

