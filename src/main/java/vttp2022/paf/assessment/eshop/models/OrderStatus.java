package vttp2022.paf.assessment.eshop.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// DO NOT CHANGE THIS CLASS
public class OrderStatus {

	// Members of OrderStatus
	private String orderId;
	private String deliveryId = "";
	private String status = "pending"; // or "dispatched"

	// Generate getter and setter
	public String getOrderId() { return this.orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getDeliveryId() { return this.deliveryId; }
	public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }

	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }

	// Create OrderStatus object from JsonObject
	public static OrderStatus create(JsonObject jo) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(jo.getString("orderId"));
		orderStatus.setDeliveryId(jo.getString("deliveryId"));
		return orderStatus;
	}

	// Create JsonObject from OrderStatus object
	public JsonObject toJSON() {
		// If status pending, return only orderId and status
		if (status.equals("pending")) {
			return Json.createObjectBuilder()
						.add("orderId", orderId)
						.add("status", status)
						.build();
		}
		// If status dispatch, return orderId, deliveryId and status 
		return Json.createObjectBuilder()
					.add("orderId", orderId)
					.add("deliveryId", deliveryId)
					.add("status", status)
					.build();
	}
}
