package com.enablix.app.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class OrderEntityController {

	@Autowired
	private OrderEntityService orderService;
	
	@RequestMapping(method = RequestMethod.POST, value="/order/swap",
			consumes = "application/json", produces = "application/json")
	public boolean swapOrder(@RequestBody SwapOrderRequest swapRequest) {
		return orderService.swapOrder(swapRequest);
	}
	
}
