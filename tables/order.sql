-- public.tbl_orders definition

-- Drop table

-- DROP TABLE public.tbl_orders;

CREATE TABLE public.tbl_orders (
	order_id varchar(20) NOT NULL,
	customer_id varchar(20) NOT NULL,
	error_message text NULL,
	order_date date NULL,
	price_per_unit numeric(10, 2) NULL,
	product_code varchar(10) NULL,
	product_name varchar(255) NULL,
	quantity int4 NULL,
	status varchar(20) NOT NULL,
	total_price numeric(10, 2) NULL,
	CONSTRAINT tbl_orders_customer_id_not_null NOT NULL customer_id,
	CONSTRAINT tbl_orders_order_id_not_null NOT NULL order_id,
	CONSTRAINT tbl_orders_pkey PRIMARY KEY (order_id),
	CONSTRAINT tbl_orders_status_not_null NOT NULL status
);
CREATE INDEX idx_orders_customer_id ON public.tbl_orders USING btree (customer_id);
CREATE INDEX idx_orders_status ON public.tbl_orders USING btree (status);