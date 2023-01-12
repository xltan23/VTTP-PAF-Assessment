package vttp2022.paf.assessment.eshop.respositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3 
		// Query selects name and returns name, address and email
		SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_SELECT_CUSTOMER_BY_NAME, name);
		List<Customer> customers = new LinkedList<>();
		while (srs.next()) {
			// Create Customer from returned results and add to customer list
			customers.add(Customer.create(srs));
		}
		Customer customer = null;
		if (customers.size() > 0) {
			customer = customers.get(0);
		} else {
			// If customer list is empty => No Results returned from query => Name does not exist
			return Optional.empty();
		}
		// Return box of one customer
		return Optional.of(customer);
	}
}
