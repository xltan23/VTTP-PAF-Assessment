package vttp2022.paf.assessment.eshop.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// DO NOT CHANGE THIS CLASS
public class OrderStatus {

	private String orderId;
	private String deliveryId = "";
	private String status = "pending"; // or "dispatched"

	public String getOrderId() { return this.orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getDeliveryId() { return this.deliveryId; }
	public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }

	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }

	public static OrderStatus create(JsonObject jo) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOrderId(jo.getString("orderId"));
		orderStatus.setDeliveryId(jo.getString("deliveryId"));
		// orderStatus.setStatus("dispatched");
		return orderStatus;
	}

	public JsonObject toJSON() {
		if (status.equals("pending")) {
			return Json.createObjectBuilder()
						.add("orderId", orderId)
						.add("status", status)
						.build();
		}
		return Json.createObjectBuilder()
					.add("orderId", orderId)
					.add("deliveryId", deliveryId)
					.add("status", status)
					.build();
	}
}
