drop schema retail_demo CASCADE ;
CREATE SCHEMA retail_demo;

-- 1. HAWQ table
DROP external TABLE IF EXISTS retail_demo.categories_dim_hawq;
CREATE external TABLE retail_demo.categories_dim_hawq
(
    category_id bigint,
    category_name character varying(400)
)
LOCATION ('pxf://namenode:51200/retail_demo.categories_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 2. HAWQ external table
DROP external table IF EXISTS retail_demo.customers_dim_hawq;
CREATE external table retail_demo.customers_dim_hawq
(
    customer_id bigint,
    first_name TEXT,
    last_name TEXT,
    gender TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.customers_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 3. HAWQ external table
DROP external table IF EXISTS retail_demo.order_lineitems_hawq;
CREATE  external table retail_demo.order_lineitems_hawq
(
    order_id TEXT,
    order_item_id bigint,
    product_id int,
    product_name TEXT,
    customer_id int,
    store_id INT ,
    item_shipment_status_code TEXT,
    order_datetime TIMESTAMP ,
    ship_datetime TIMESTAMP,
    item_return_datetime TIMESTAMP,
    item_refund_datetime TIMESTAMP,
    product_category_id int,
    product_category_name TEXT,
    payment_method_code TEXT,
    tax_amount DOUBLE  PRECISION ,
    item_quantity INT ,
    item_price DOUBLE  PRECISION ,
    discount_amount DOUBLE  PRECISION ,
    coupon_code TEXT,
    coupon_amount DOUBLE  PRECISION ,
    ship_address_line1 TEXT,
    ship_address_line2 TEXT,
    ship_address_line3 TEXT,
    ship_address_city TEXT,
    ship_address_state TEXT,
    ship_address_postal_code TEXT,
    ship_address_country TEXT,
    ship_phone_number TEXT,
    ship_customer_name TEXT,
    ship_customer_email_address TEXT,
    ordering_session_id TEXT,
    website_url TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.order_lineitems_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 4. HAWQ external table
DROP external table IF EXISTS retail_demo.orders_hawq;
CREATE external table retail_demo.orders_hawq
(
    order_id TEXT,
    customer_id int,
    store_id int,
    order_datetime TEXT,
    ship_completion_datetime TEXT,
    return_datetime TEXT,
    refund_datetime TEXT,
    payment_method_code TEXT,
    total_tax_amount DOUBLE PRECISION ,
    total_paid_amount DOUBLE PRECISION ,
    total_item_quantity int,
    total_discount_amount DOUBLE PRECISION ,
    coupon_code TEXT,
    coupon_amount DOUBLE PRECISION ,
    order_canceled_flag TEXT,
    has_returned_items_flag TEXT,
    has_refunded_items_flag TEXT,
    fraud_code TEXT,
    fraud_resolution_code TEXT,
    billing_address_line1 TEXT,
    billing_address_line2 TEXT,
    billing_address_line3 TEXT,
    billing_address_city TEXT,
    billing_address_state TEXT,
    billing_address_postal_code TEXT,
    billing_address_country TEXT,
    billing_phone_number TEXT,
    customer_name TEXT,
    customer_email_address TEXT,
    ordering_session_id TEXT,
    website_url TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.orders_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 5. HAWQ external table
DROP external table IF EXISTS retail_demo.customer_addresses_dim_hawq;
CREATE external table retail_demo.customer_addresses_dim_hawq
(
    customer_address_id bigint,
    customer_id bigint,
    valid_from_timestamp TEXT,
    valid_to_timestamp TEXT,
    house_number TEXT,
    street_name TEXT,
    appt_suite_no TEXT,
    city TEXT,
    state_code TEXT,
    zip_code TEXT,
    zip_plus_four TEXT,
    country TEXT,
    phone_number TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.customer_addresses_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 6. HAWQ external table
DROP external table IF EXISTS retail_demo.date_dim_hawq;
CREATE external table retail_demo.date_dim_hawq
(
    calendar_day date,
    reporting_year smallint,
    reporting_quarter smallint,
    reporting_month smallint,
    reporting_week smallint,
    reporting_dow smallint
)
LOCATION ('pxf://namenode:51200/retail_demo.date_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 7. HAWQ external table
DROP external table IF EXISTS retail_demo.email_addresses_dim_hawq;
CREATE external table retail_demo.email_addresses_dim_hawq
(
    customer_id int,
    email_address TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.email_addresses_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;


-- 8. HAWQ external table
DROP external table IF EXISTS retail_demo.payment_methods_hawq;
CREATE external table retail_demo.payment_methods_hawq
(
    payment_method_id smallint,
    payment_method_code character varying(20)
)
LOCATION ('pxf://namenode:51200/retail_demo.payment_methods_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

-- 9. HAWQ external table
DROP external table IF EXISTS retail_demo.products_dim_hawq;
CREATE external table retail_demo.products_dim_hawq
(
    product_id int,
    category_id SMALLINT ,
    price DOUBLE PRECISION ,
    product_name TEXT
)
LOCATION ('pxf://namenode:51200/retail_demo.products_dim_hive?profile=Hive') FORMAT 'custom' (formatter='pxfwritable_import');;

