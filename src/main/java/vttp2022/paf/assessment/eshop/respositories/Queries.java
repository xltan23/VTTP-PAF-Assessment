package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
    
    public static final String SQL_SELECT_CUSTOMER_BY_NAME = "SELECT name, address, email FROM customers WHERE name = ?";

    public static final String SQL_INSERT_ORDER = "INSERT INTO orders(id, name, email, address, status, order_date) VALUES (?,?,?,?,?,SYSDATE())";

    public static final String SQL_INSERT_LINE_ITEMS = "INSERT INTO line_items(order_id, item, quantity) VALUES (?,?,?)";

    public static final String SQL_INSERT_ORDER_STATUS = "INSERT INTO order_status(order_id, delivery_id, status, status_update) VALUES (?,?,?,SYSDATE())";
}
