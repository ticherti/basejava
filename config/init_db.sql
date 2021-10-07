CREATE TABLE IF NOT EXISTS resume
(
    uuid char
(
    36
) NOT NULL
    CONSTRAINT resume_pk
    PRIMARY KEY,
    full_name text NOT NULL
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

CREATE
UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, type);

create table section
(
    id          serial   not null
        constraint sections_pk
            primary key,
    resume_uuid char(36) not null
        constraint sections_resume_uuid_fk
            references resume
            on delete cascade,
    type        char(36) not null,
    value       text     not null
);

alter table section owner to postgres;

create
unique index section_type_resume_uuid_uindex
    on sections (type, resume_uuid);
