create table post (
    id serial primary key,
    name text
);

create table candidate (
    id serial primary key,
    name text,
    photo_id int references photo(id)
);

create table photo (
    id serial primary key
);