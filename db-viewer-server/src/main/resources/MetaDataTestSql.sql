CREATE TABLE IF NOT EXISTS public.database_connection_data (
   id SERIAL PRIMARY KEY,
   name VARCHAR(200) NOT NULL,
   hostname VARCHAR(200) NOT NULL,
   port INTEGER NOT NULL,
   database_name VARCHAR(200) NOT NULL,
   username VARCHAR(200) NOT NULL,
   password VARCHAR(200) NOT NULL
);

INSERT INTO public.database_connection_data (id,name,hostname,port,database_name,username,password)
VALUES (9999,'dbviewer','localhost',5432,'dbviewer','postgres','lalala');