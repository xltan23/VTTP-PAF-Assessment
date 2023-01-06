package vttp2022.paf.assessment.eshop.models;

// DO NOT CHANGE THIS CLASS
public class LineItem {

	private String item;
	private Integer quantity;

	public String getItem() { return this.item; }
	public void setItem(String item) { this.item = item; }

	public Integer getQuantity() { return this.quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }

	public static LineItem create(String item, Integer quantity) {
		LineItem lineItem = new LineItem();
		lineItem.setItem(item);
		lineItem.setQuantity(quantity);
		return lineItem;
	}
}
