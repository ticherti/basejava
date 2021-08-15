CREATE TABLE IF NOT EXISTS resume
(
    uuid      char(36) NOT NULL
        CONSTRAINT resume_pk
            PRIMARY KEY,
    full_name text     NOT NULL
);

CREATE TABLE contact
(
    id          serial NOT NULL
        CONSTRAINT contact_pk
            PRIMARY KEY,
    resume_uuid char(36)
        CONSTRAINT contact_resume_uuid_fk
            REFERENCES resume
            ON DELETE CASCADE,
    type        text   NOT NULL,
    value       text   NOT NULL
);

ALTER TABLE contact
    OWNER TO postgres;

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

CREATE TABLE personal
(
    id          serial   NOT NULL,
    resume_uuid char(36) NOT NULL
        CONSTRAINT personal_contact_uuid_fk
            REFERENCES resume
            ON DELETE CASCADE,
    value       text     NOT NULL
);

CREATE UNIQUE INDEX personal_id_uindex
    ON personal (id);

ALTER TABLE personal
    ADD CONSTRAINT personal_pk
        PRIMARY KEY (id);

create table objective
(
    id serial not null
        constraint objective_pk
            primary key,
    resume_uuid char(36) not null
        constraint objective_resume_uuid_fk
            references resume
            on delete cascade,
    value text not null
);

create unique index objective_resume_uuid_uindex
    on objective (resume_uuid);
create table qualification
(
    id serial not null
        constraint qualification_pk
            primary key,
    resume_uuid char(36) not null
        constraint qualification_resume_uuid_fk
            references resume
            on delete cascade,
    value text not null
);

create unique index qualifications_resume_uuid_uindex
    on qualifications (resume_uuid);
create table qualifications
(
    id serial not null
        constraint qualifications_pk
            primary key,
    resume_uuid char(36) not null
        constraint qualifications_resume_uuid_fk
            references resume
            on delete cascade,
    value text not null
);

create unique index qualifications_resume_uuid_uindex
    on qualification (resume_uuid);

create table achievement
(
    id serial not null
        constraint achievement_pk
            primary key,
    resume_uuid char(36) not null
        constraint achievement_resume_uuid_fk
            references resume
            on delete cascade,
    value text not null
);

create unique index achievement_resume_uuid_uindex
    on achievement (resume_uuid);

