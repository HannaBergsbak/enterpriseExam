insert into users
values (nextval('users_user_id_seq'), 'Name', '$2a$12$.m1KB1Ag2migpQ9pQ/LyXeBqSkj3kD0lI9BQ/Gw5DSb4pVAGOfPfy', true);

insert into authorities
values (nextval('authorities_authority_id_seq'), 'USER');

insert into authorities
values (nextval('authorities_authority_id_seq'), 'ADMIN');

insert into users_authorities
values (1, 1);

insert into users_authorities
values (1, 2);