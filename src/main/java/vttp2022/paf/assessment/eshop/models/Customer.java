package vttp2022.paf.assessment.eshop.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// DO NOT CHANGE THIS CLASS
public class Customer {

	private String name;
	private String address;
	private String email;

	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }

	public String getAddress() { return this.address; }
	public void setAddress(String address) { this.address = address; }

	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }

	// Create customer object from SqlRowSet
	public static Customer create(SqlRowSet srs) {
		Customer customer = new Customer();
		customer.setName(srs.getString("name"));
		customer.setAddress(srs.getString("address"));
		customer.setEmail(srs.getString("email"));
		return customer;
	}

	// Create JsonObject from Customer object
	public JsonObject toJSON() {
		return Json.createObjectBuilder()
					.add("name", name)
					.add("address", address)
					.add("email", email)
					.build();
	}
}
