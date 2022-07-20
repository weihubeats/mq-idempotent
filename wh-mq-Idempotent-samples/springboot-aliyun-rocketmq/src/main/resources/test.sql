create table test
(
    id       bigserial
        constraint test_pk
            primary key,
    msg      text,
    add_time timestamp default CURRENT_TIMESTAMP not null
);