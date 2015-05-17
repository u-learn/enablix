enablix.filters.filter('ebxFormatData', function($filter) {
	
	return function(input, dataType) {
		
		if (isNullOrUndefined(input)) { return ""; }
		
		var text = input;
		
		switch(dataType) {
			
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
					
						text += input[i].label;
					}
				}
				break;
		}
		
		return text;
	};
	
});