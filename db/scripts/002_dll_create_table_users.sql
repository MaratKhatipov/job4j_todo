create table todo_users(
    id serial primary key,
    name varchar(20) not null ,
    login varchar(50) not null unique,
    password varchar(50)
);