create table users(
    user_id bigserial primary key,
    user_name varchar(50),
    user_password varchar(100),
    user_enabled bool
);

create table authorities(
    authority_id bigserial primary key,
    authority_name varchar(50)
);

create table users_authorities(
    user_id bigint,
    authority_id bigint
);
