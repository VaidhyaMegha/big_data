#!/bin/bash -x

#http://pivotalhd-210.docs.pivotal.io/tutorial/getting-started/dataset.html
#https://github.com/gopivotal/pivotal-samples/tree/master/sample-data
#
#Schema :
#The data set includes the following entities:
#
# ENTITY	DESCRIPTION
# ----------------------
# customers_dim	Customer Data
# email_addresses_dim	Email addresses of customers
# product_dim	Product Details
# customer_addresses_dim	Address of each customer
# order_lineitems	Line Item for each Order
# orders	Details of an Order
# categories_dim	Category for each Product

hdfs dfs -rm -r /retail_demo

# copy and rename pivotal-samples/sample-data folder to retail_demo and then run below command
hdfs dfs -put retail_demo /

hdfs dfs -ls /retail_demo

hdfs fsck /retail_demo

hdfs dfs -chmod -R 777 /retail_demo
