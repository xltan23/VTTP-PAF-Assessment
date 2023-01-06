-- customers table 
CREATE TABLE customers (
	name VARCHAR(32) not null,
    email VARCHAR(128) not null,
    address VARCHAR(128) not null,
    primary key(name)
);

-- Insert statements
INSERT INTO customers (name, address, email) VALUES ("fred", "201 Cobblestone Lane", "fredflintstone@bedrock.com");
INSERT INTO customers (name, address, email) VALUES ("sherlock", "221B Baker Street, London", "sherlock@consultingdetective.org");
INSERT INTO customers (name, address, email) VALUES ("spongebob", "124 Conch Street, Bikini Bottom", "spongebob@yahoo.com");
INSERT INTO customers (name, address, email) VALUES ("jessica", "698 Candlewood Land, Cabot Cove", "fletcher@gmail.com");
INSERT INTO customers (name, address, email) VALUES ("dursley", "4 Privet Drive, Little Whinging, Surrey", "dursley@gmail.com");

-- orders table
CREATE TABLE orders (
	id VARCHAR(8) not null,
    delivery_id VARCHAR(8) not null,
	name VARCHAR(32) not null,
    email VARCHAR(128) not null,
    address VARCHAR(128) not null,
    status VARCHAR(128) not null,
    order_date Date not null,
    primary key(id)
);

-- line_items table
CREATE TABLE line_items (
	order_id VARCHAR(8) not null,
    item VARCHAR(128) not null,
    quantity int not null,
    constraint fk_order_id foreign key(order_id) references orders(id)
);