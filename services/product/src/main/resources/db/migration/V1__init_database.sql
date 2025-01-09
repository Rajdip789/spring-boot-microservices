create table if not exists category
(
    id integer not null primary key,
    description varchar(255),
    name varchar(255)
);

create table if not exists product
(
    id integer not null primary key,
    description varchar(255),
    name varchar(255),
    available_quantity double precision not null,
    price numeric(28, 2),
    category_id integer,
    CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES category(id)
);

create sequence if not exists category_seq increment by 50;
create sequence if not exists product_seq increment by 50;