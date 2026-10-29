-- public.tbl_products definition

-- Drop table

-- DROP TABLE public.tbl_products;

CREATE TABLE public.tbl_products (
	product_code varchar(10) NOT NULL,
	product_name varchar(255) NOT NULL,
	CONSTRAINT tbl_products_pkey PRIMARY KEY (product_code),
	CONSTRAINT tbl_products_product_code_not_null NOT NULL product_code,
	CONSTRAINT tbl_products_product_name_not_null NOT NULL product_name
);