package vttp2022.paf.assessment.eshop.respositories;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// TODO: Task 3
	public boolean insertOrder(Order order) {
		order.setStatus("pending");
		List<LineItem> lineItems = order.getLineItems();

		// Create list of object array
		List<Object[]> objArrayList = lineItems.stream()
												.map(lineItem -> {
													// Map each line item into an object array
													Object[] li = new Object[3];
													li[0] = order.getOrderId();
													li[1] = lineItem.getItem();
													li[2] = lineItem.getQuantity();
													return li;
												}).toList();
		// Perform insertion on both tables
		Integer numRows = jdbcTemplate.update(SQL_INSERT_ORDER, order.getOrderId(), order.getName(), order.getEmail(), order.getAddress(), order.getStatus());
		int[] array = jdbcTemplate.batchUpdate(SQL_INSERT_LINE_ITEMS, objArrayList);
		return (numRows > 0 && array.length > 0);
	} 
}
