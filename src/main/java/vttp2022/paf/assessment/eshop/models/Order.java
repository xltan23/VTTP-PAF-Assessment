package vttp2022.paf.assessment.eshop.models;

import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

// DO NOT CHANGE THIS CLASS
public class Order {

	// Members of Order
	private String orderId;
	private String deliveryId;
	private String name;
	private String address;
	private String email;
	private String status;
	private Date orderDate = new Date();
	private List<LineItem> lineItems = new LinkedList<>();

	// Generate getter and setter
	public String getOrderId() { return this.orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getDeliveryId() { return this.deliveryId; }
	public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }

	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }

	public String getAddress() { return this.address; }
	public void setAddress(String address) { this.address = address; }

	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }

	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }

	public Date getOrderDate() { return this.orderDate; }
	public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

	public Customer getCustomer() { 
		Customer customer = new Customer();
		customer.setName(name);
		customer.setAddress(address);
		customer.setEmail(email);
		return customer;
	}
	public void setCustomer(Customer customer) {
		name = customer.getName();
		address = customer.getAddress();
		email = customer.getEmail();
	}

	public List<LineItem> getLineItems() { return this.lineItems; }
	public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }
	public void addLineItem(LineItem lineItem) { this.lineItems.add(lineItem); }

	// Create Order Object from JsonObject
	public static Order create(JsonObject jo) {
		// Order: orderId, name, address, email, lineItems
		// Not in Order: deliveryId, status, orderDate (retrieved from query)
		Order order = new Order();
		// Upon Order creation, a UUID is generated
		order.setOrderId(UUID.randomUUID().toString().substring(0,8));
		order.setName(jo.getString("name"));
		order.setAddress(jo.getString("address"));
		order.setEmail(jo.getString("email"));
		List<LineItem> lineItemList = new LinkedList<>();
		// JsonObject consist of "lineItems" : [{"item":"","quantity":""},{"item":"","quantity":""}]
		JsonArray lineItems = jo.getJsonArray("lineItems");
		for (JsonValue lineItem : lineItems) {
			String item = lineItem.asJsonObject().getString("item");
			Integer quantity = lineItem.asJsonObject().getInt("quantity");
			// Create LineItem object and add to list
			lineItemList.add(LineItem.create(item, quantity));
		}
		order.setLineItems(lineItemList);
		return order;
	}

	// Create Order Object from JsonString (String posted via. Postman)
	public static Order create(String jsonString) {
		StringReader sr = new StringReader(jsonString);
		JsonReader jr = Json.createReader(sr);
		return create(jr.readObject());
	}

	// Convert Order into JsonObject payload to be posted to server
	public JsonObject toJSON() {
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (LineItem lineItem : lineItems) {
			jab.add(lineItem.toJSON());
		}
		JsonArray ja = jab.build();
		return Json.createObjectBuilder()
					.add("orderId", orderId)
					.add("name", name)
					.add("address", address)
					.add("email", email)
					.add("lineItems", ja)
					.add("createdBy", "Tan Xian Liang")
					.build();
	}
}

