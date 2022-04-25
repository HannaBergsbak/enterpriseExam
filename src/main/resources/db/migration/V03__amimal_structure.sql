create table animals(
    animal_id bigserial primary key,
    animal_name varchar(50),
    animal_type varchar(50),
    breed varchar(50),
    age int,
    healthy bool
);