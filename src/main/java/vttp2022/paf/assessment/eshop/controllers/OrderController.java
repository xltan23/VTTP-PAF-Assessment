package vttp2022.paf.assessment.eshop.controllers;

import java.util.LinkedList;
import java.util.List;
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
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private OrderRepository orderRepo;

	//TODO: Task 3
	@GetMapping(path = "/customer/{name}")
	public ResponseEntity<String> findCustomer(@PathVariable String name) {
		Optional<Customer> optCustomer = customerRepo.findCustomerByName(name);
		if (optCustomer.isEmpty()) {
			JsonObject errorMsg = Json.createObjectBuilder()
										.add("error", "Customer " + name + " not found")
										.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.contentType(MediaType.APPLICATION_JSON)
								.body(errorMsg.toString());
		}
		JsonObject customerResult = Json.createObjectBuilder()
										.add("Customer", optCustomer.get().toJSON())
										.build();
		return ResponseEntity.status(HttpStatus.FOUND)
							.contentType(MediaType.APPLICATION_JSON)
							.body(customerResult.toString());
	}

	@PostMapping(path = "/checkout")
	public ResponseEntity<String> checkoutOrder(@RequestBody String jsonString) {
		// Json String comprise of following info:
		// Name, Address, Email, LineItem JsonArray
		Order order = Order.create(jsonString);
		// Upon creation, Order ID is added
		Boolean success = orderRepo.insertOrder(order);
		if (!success) {
			JsonObject errorMsg = Json.createObjectBuilder()
								.add("error", "did not successfully insert order")
								.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.contentType(MediaType.APPLICATION_JSON)
								.body(errorMsg.toString());
		} 
		JsonObject jo = Json.createObjectBuilder()
							.add("success", "order inserted")
							.build();
		return ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(jo.toString());
	}
}
