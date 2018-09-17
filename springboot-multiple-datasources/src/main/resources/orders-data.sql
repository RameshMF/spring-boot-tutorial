delete from order_items;
delete from orders;

insert into orders(id, cust_name, cust_email) values(1,'Ramesh','ramesh@gmail.com');
insert into orders(id, cust_name, cust_email) values(2,'Jonn','sanjay@gmail.com');
insert into orders(id, cust_name, cust_email) values(3,'Salman','salman@gmail.com');

insert into order_items(id, productcode,quantity,order_id) values(1,'P100', 2, 1);
insert into order_items(id, productcode,quantity,order_id) values(2,'P101', 1, 1);
insert into order_items(id, productcode,quantity,order_id) values(3,'P102', 5, 1);
