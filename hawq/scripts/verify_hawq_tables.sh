#!/bin/bash -x

customers_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.customers_dim_hawq;"`
categories_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.categories_dim_hawq;"`
date_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.date_dim_hawq;"`
email_addresses_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.email_addresses_dim_hawq;"`
order_lineitems_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.order_lineitems_hawq;"`
orders_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.orders_hawq;"`
payment_methods_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.payment_methods_hawq;"`
products_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.products_dim_hawq;"`
customer_addresses_dim_hawq=`psql -h hawq_master -p 10432 -d template1 -Atc "select count(*) from retail_demo.customer_addresses_dim_hawq;"`
