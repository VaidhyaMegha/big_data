drop schema retail_demo CASCADE ;
CREATE SCHEMA retail_demo;

-- 3. HAWQ external table
DROP external table IF EXISTS retail_demo.order_lineitems_hawq;
CREATE  external table retail_demo.order_lineitems_hawq
(
    recordkey bytea,
    "cf1:order_id" TEXT,
    "cf1:order_item_id" bigint,
    "cf1:product_id" int,
    "cf1:product_name" TEXT,
    "cf1:customer_id" int,
    "cf1:store_id" INT ,
    "cf1:item_shipment_status_code" TEXT,
    "cf1:order_datetime" TIMESTAMP ,
    "cf1:ship_datetime" TIMESTAMP,
    "cf1:item_return_datetime" TIMESTAMP,
    "cf1:item_refund_datetime" TIMESTAMP,
    "cf1:product_category_id" int,
    "cf1:product_category_name" TEXT,
    "cf1:payment_method_code" TEXT,
    "cf1:tax_amount" DOUBLE  PRECISION ,
    "cf1:item_quantity" INT ,
    "cf1:item_price" DOUBLE  PRECISION ,
    "cf1:discount_amount" DOUBLE  PRECISION ,
    "cf1:coupon_code" TEXT,
    "cf1:coupon_amount" DOUBLE  PRECISION ,
    "cf1:ship_address_line1" TEXT,
    "cf1:ship_address_line2" TEXT,
    "cf1:ship_address_line3" TEXT,
    "cf1:ship_address_city" TEXT,
    "cf1:ship_address_state" TEXT,
    "cf1:ship_address_postal_code" TEXT,
    "cf1:ship_address_country" TEXT,
    "cf1:ship_phone_number" TEXT,
    "cf1:ship_customer_name" TEXT,
    "cf1:ship_customer_email_address" TEXT,
    "cf1:ordering_session_id" TEXT,
    "cf1:website_url" TEXT
)
LOCATION ('pxf://namenode:51200/order_lineitems_hawq?profile=Hbase') FORMAT 'custom' (formatter='pxfwritable_import');
