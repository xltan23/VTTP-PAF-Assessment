package vttp2022.paf.assessment.eshop.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Service
public class WarehouseService {

	private static final String URL = "http://paf.chuklee.com/dispatch/";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {
		String payload = "";
		// TODO: Task 4
		try {
			// Building URL: http://paf.chuklee.com/dispatch/{orderId}
			String url = UriComponentsBuilder.fromUriString(URL)
						.path(order.getOrderId())
						.toUriString();
			System.out.println(url);
			// Create request: JsonObject 
			JsonObject orderRequest = order.toJSON();
			System.out.println(orderRequest.toString());
			// RequestEntity<JsonObject> req = RequestEntity.post("http://paf.chuklee.com/dispatch/{var}", order.getOrderId()).accept(MediaType.APPLICATION_JSON).body(orderRequest);
			RequestEntity<JsonObject> req = RequestEntity.post(url).accept(MediaType.APPLICATION_JSON).body(orderRequest);
			// Make the call to server 
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> resp;
			resp = template.exchange(req, String.class);
			payload = resp.getBody(); 
			System.out.println(payload);
		} catch (Exception e) {
			// If dispatch failed:
			System.err.printf("Error: %s\n", e.getMessage());
			OrderStatus pendingOrder = new OrderStatus();
			pendingOrder.setOrderId(order.getOrderId());
			pendingOrder.setDeliveryId("");
			pendingOrder.setStatus("pending");
			jdbcTemplate.update(SQL_INSERT_ORDER_STATUS, pendingOrder.getOrderId(), pendingOrder.getDeliveryId(), pendingOrder.getStatus());
			return pendingOrder;
		}
		// If dispatch successful:
		// Read payload returned by server
		StringReader sr = new StringReader(payload);
		JsonReader jr = Json.createReader(sr);
		JsonObject jo = jr.readObject();
		OrderStatus dispatchedOrder = new OrderStatus();
		dispatchedOrder = OrderStatus.create(jo);
		dispatchedOrder.setStatus("dispatched");
		jdbcTemplate.update(SQL_INSERT_ORDER_STATUS, dispatchedOrder.getOrderId(), dispatchedOrder.getDeliveryId(), dispatchedOrder.getStatus());
		return dispatchedOrder;
	}
}
