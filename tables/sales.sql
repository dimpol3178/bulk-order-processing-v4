-- public.tbl_sales_report definition

-- Drop table

-- DROP TABLE public.tbl_sales_report;

CREATE TABLE public.tbl_sales_report (
	customer_id varchar(20) NOT NULL,
	total_spent numeric(10, 2) NOT NULL,
	CONSTRAINT tbl_sales_report_customer_id_not_null NOT NULL customer_id,
	CONSTRAINT tbl_sales_report_pkey PRIMARY KEY (customer_id),
	CONSTRAINT tbl_sales_report_total_spent_not_null NOT NULL total_spent
);