
CREATE TABLE IF NOT EXISTS user_management(

    id serial PRIMARY KEY,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    email varchar(100) NOT NULL,
    address varchar(200) NULL,
    date_ins timestamp(6) with time zone NOT NULL,
    date_upd timestamp(6) with time zone NOT NULL,
    date_del timestamp(6) with time zone
);

CREATE INDEX user_management_idx ON user_management USING GIN (to_tsvector('italian', first_name || ' ' || last_name));
