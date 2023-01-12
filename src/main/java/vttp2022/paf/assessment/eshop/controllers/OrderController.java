package vttp2022.paf.assessment.eshop.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private WarehouseService warehseSvc;

	//TODO: Task 3
	// localhost:8080/api/order/customer/{name}
	@GetMapping(path = "/customer/{name}")
	public ResponseEntity<String> findCustomer(@PathVariable String name) {
		Optional<Customer> optCustomer = customerRepo.findCustomerByName(name);
		if (optCustomer.isEmpty()) {
			JsonObject notFound = Json.createObjectBuilder()
										.add("error", "Customer " + name + " not found")
										.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.contentType(MediaType.APPLICATION_JSON)
								.body(notFound.toString());
		}
		// Generate customer result to test method
		JsonObject customerResult = Json.createObjectBuilder()
										.add("Customer", optCustomer.get().toJSON())
										.build();
		return ResponseEntity.status(HttpStatus.FOUND)
							.contentType(MediaType.APPLICATION_JSON)
							.body(customerResult.toString());
	}

	// localhost:8080/api/order/checkout
	@PostMapping(path = "/checkout")
	public ResponseEntity<String> checkoutOrder(@RequestBody String jsonString) {
		// JsonString comprise of following info:
		// Name, Address, Email, LineItem JsonArray 
		Order order = Order.create(jsonString);
		// Upon creation, Order ID is added
		Boolean success = orderRepo.insertOrder(order);
		if (!success) {
			JsonObject failInsert = Json.createObjectBuilder()
								.add("error", "did not successfully insert order")
								.build();
			return ResponseEntity.status(HttpStatus.OK)
								.contentType(MediaType.APPLICATION_JSON)
								.body(failInsert.toString());
		} 
		JsonObject successInsert = Json.createObjectBuilder()
							.add("success", "order inserted")
							.build();
		return ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(successInsert.toString());
	}

	// localhost:8080/api/order/checkout/dispatch
	// JsonString comprise of following info:
	// Name, Address, Email, LineItem JsonArray
	@PostMapping(path = "/checkout/dispatch")
	public ResponseEntity<String> checkoutAndDispatchOrder(@RequestBody String jsonString) {
		Order order = Order.create(jsonString);
		OrderStatus orderStatus = warehseSvc.dispatch(order);
		order.setStatus(orderStatus.getStatus());
		Boolean success = orderRepo.insertOrder(order);
		if (!success) {
			JsonObject failInsert = Json.createObjectBuilder()
								.add("error", "did not successfully insert order")
								.build();
			return ResponseEntity.status(HttpStatus.OK)
								.contentType(MediaType.APPLICATION_JSON)
								.body(failInsert.toString());
		} 
		JsonObject statusMsg = null;
		
		// If order successfuly inserted: check if order has been dispatched
		if (orderStatus.getStatus().equals("pending")) {
			statusMsg = Json.createObjectBuilder()
								.add("Order Status", orderStatus.toJSON())
								.build();
			return ResponseEntity.status(HttpStatus.OK)
								.contentType(MediaType.APPLICATION_JSON)
								.body(statusMsg.toString());
		} 
		statusMsg = Json.createObjectBuilder()
							.add("Order Status", orderStatus.toJSON())
							.build();
		return ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(statusMsg.toString());
	}

	// localhost:8080/api/order/{name}/status
	@GetMapping(path = "{name}/status")
	public ResponseEntity<String> getCustomerOrders(@PathVariable String name) {
		JsonObject customerOrders = orderRepo.getCustomerOrders(name);
		return ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(customerOrders.toString());
	}
}
