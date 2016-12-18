CREATE USER c##prod IDENTIFIED BY Riephiepaex4;
CREATE USER c##test IDENTIFIED BY Fah0pooxietu;

GRANT CREATE SESSION,
UNLIMITED TABLESPACE
TO c##prod;

GRANT CREATE SESSION,
UNLIMITED TABLESPACE
TO c##test;

/*USERS TABLE*/
/*PROD SCHEMA*/
CREATE TABLE c##prod.users (
  user_id                RAW(32) DEFAULT SYS_GUID() NOT NULL,
  first_name             NVARCHAR2(255)             NOT NULL,
  last_name              NVARCHAR2(255)             NOT NULL,
  password_hash          NUMBER(10)                 NOT NULL,
  email                  VARCHAR2(127)              NOT NULL,
  email_validated        NUMBER(1) DEFAULT 0        NOT NULL,
  email_validation_token RAW(32) DEFAULT SYS_GUID() NOT NULL,
  CONSTRAINT user_pk PRIMARY KEY (user_id),
  CONSTRAINT user_unique_email UNIQUE (email),
  CONSTRAINT user_unique_validation_token UNIQUE (email_validation_token)
);

CREATE INDEX c##prod.index_users ON c##prod.users (
  email,
  password_hash,
  email_validated
);

GRANT SELECT ON c##prod.users TO c##prod;

/*TEST SCHEMA*/
CREATE TABLE c##test.users (
  user_id                RAW(32) DEFAULT SYS_GUID() NOT NULL,
  first_name             NVARCHAR2(255)             NOT NULL,
  last_name              NVARCHAR2(255)             NOT NULL,
  password_hash          NUMBER(10)                 NOT NULL,
  email                  VARCHAR2(255)              NOT NULL,
  email_validated        NUMBER(1) DEFAULT 0        NOT NULL,
  email_validation_token RAW(32) DEFAULT SYS_GUID() NOT NULL,
  CONSTRAINT user_pk PRIMARY KEY (user_id),
  CONSTRAINT user_unique_email UNIQUE (email),
  CONSTRAINT user_unique_validation_token UNIQUE (email_validation_token)
);

CREATE INDEX c##test.index_users ON c##test.users (
  email,
  password_hash,
  email_validated
);

GRANT SELECT ON c##test.users TO c##test;

/*AUTH_TOKENS TABLE*/
/*PROD SCHEMA*/
CREATE TABLE c##prod.auth_tokens (
  token_id RAW(32) DEFAULT SYS_GUID() NOT NULL,
  user_id  RAW(32)                    NOT NULL,
  CONSTRAINT auth_token_pk PRIMARY KEY (token_id),
  CONSTRAINT auth_token_unique_user UNIQUE (user_id),
  CONSTRAINT auth_token_fk_user FOREIGN KEY (user_id) REFERENCES c##prod.users (user_id) ON DELETE CASCADE
);

GRANT SELECT ON c##prod.auth_tokens TO c##prod;

/*TEST SCHEMA*/
CREATE TABLE c##test.auth_tokens (
  token_id RAW(32) DEFAULT SYS_GUID() NOT NULL,
  user_id  RAW(32)                    NOT NULL,
  CONSTRAINT auth_token_pk PRIMARY KEY (token_id),
  CONSTRAINT auth_token_unique_user UNIQUE (user_id),
  CONSTRAINT auth_token_fk_user FOREIGN KEY (user_id) REFERENCES c##test.users (user_id) ON DELETE CASCADE
);

GRANT SELECT ON c##test.auth_tokens TO c##test;

/*CALENDARS TABLE*/
/*PROD SCHEMA*/
CREATE TABLE c##prod.calendars (
  calendar_id RAW(32) DEFAULT SYS_GUID() NOT NULL,
  name        NVARCHAR2(255)             NOT NULL,
  description NVARCHAR2(1024)            NULL,
  color       VARCHAR2(6)                NOT NULL,
  visible     NUMBER(1) DEFAULT 1        NOT NULL,
  deleted     NUMBER(1) DEFAULT 0        NOT NULL,
  required    NUMBER(1) DEFAULT 0        NOT NULL,
  timezone    VARCHAR2(6)                NULL,
  user_id     RAW(32)                    NOT NULL,
  CONSTRAINT calendar_pk PRIMARY KEY (calendar_id),
  CONSTRAINT calendars_fk_user FOREIGN KEY (user_id) REFERENCES c##prod.users (user_id) ON DELETE CASCADE
);

CREATE INDEX c##prod.index_calendars ON c##prod.calendars (
  visible,
  deleted,
  user_id
);

GRANT SELECT ON c##prod.calendars TO c##prod;

/*TEST SCHEMA*/
CREATE TABLE c##test.calendars (
  calendar_id RAW(32) DEFAULT SYS_GUID() NOT NULL,
  name        NVARCHAR2(255)             NOT NULL,
  description NVARCHAR2(1024)            NULL,
  color       VARCHAR2(6)                NOT NULL,
  visible     NUMBER(1) DEFAULT 1        NOT NULL,
  deleted     NUMBER(1) DEFAULT 0        NOT NULL,
  required    NUMBER(1) DEFAULT 0        NOT NULL,
  timezone    VARCHAR2(6)                NULL,
  user_id     RAW(32)                    NOT NULL,
  CONSTRAINT calendar_pk PRIMARY KEY (calendar_id),
  CONSTRAINT calendars_fk_user FOREIGN KEY (user_id) REFERENCES c##test.users (user_id) ON DELETE CASCADE
);

CREATE INDEX c##test.index_calendars ON c##test.calendars (
  visible,
  deleted,
  user_id
);

GRANT SELECT ON c##test.calendars TO c##test;

/*EVENTS TABLE*/
/*PROD SCHEMA*/
CREATE TABLE c##prod.events (
  event_id         RAW(32) DEFAULT SYS_GUID() NOT NULL,
  name             NVARCHAR2(255)             NOT NULL,
  description      NVARCHAR2(1024)            NULL,
  location         NVARCHAR2(1024)            NULL,
  deleted          NUMBER(1) DEFAULT 0        NOT NULL,
  color            VARCHAR2(6)                NOT NULL,
  parent_event_id  RAW(32)                    NULL,
  start_time_utc   DATE                       NOT NULL,
  start_time_tz    VARCHAR2(6)                NULL,
  end_time_utc     DATE                       NOT NULL,
  end_time_tz      VARCHAR2(6)                NULL,
  rec_end_time_utc DATE                       NULL,
  rec_end_time_tz  VARCHAR2(6)                NULL,
  rec_period       NUMBER(3)                  NULL,
  rec_type_id      NUMBER(2)                  NULL,
  show_in_series   NUMBER(1)                  NULL,
  calendar_id      RAW(32)                    NOT NULL,
  CONSTRAINT event_pk PRIMARY KEY (event_id),
  CONSTRAINT event_parent_fk FOREIGN KEY (parent_event_id) REFERENCES c##prod.events (event_id) ON DELETE CASCADE,
  CONSTRAINT event_fk_calendar FOREIGN KEY (calendar_id) REFERENCES c##prod.calendars (calendar_id) ON DELETE CASCADE
);

CREATE INDEX c##prod.index_events ON c##prod.events (
  deleted,
  parent_event_id,
  show_in_series,
  calendar_id,
  rec_type_id,
  start_time_utc,
  start_time_tz,
  end_time_utc,
  rec_end_time_utc
);

GRANT SELECT ON c##prod.events TO c##prod;

/*TEST SCHEMA*/
CREATE TABLE c##test.events (
  event_id         RAW(32) DEFAULT SYS_GUID() NOT NULL,
  name             NVARCHAR2(255)             NOT NULL,
  description      NVARCHAR2(1024)            NULL,
  location         NVARCHAR2(1024)            NULL,
  deleted          NUMBER(1) DEFAULT 0        NOT NULL,
  color            VARCHAR2(6)                NOT NULL,
  parent_event_id  RAW(32)                    NULL,
  start_time_utc   DATE                       NOT NULL,
  start_time_tz    VARCHAR2(6)                NULL,
  end_time_utc     DATE                       NOT NULL,
  end_time_tz      VARCHAR2(6)                NULL,
  rec_end_time_utc DATE                       NULL,
  rec_end_time_tz  VARCHAR2(6)                NULL,
  rec_period       NUMBER(3)                  NULL,
  rec_type_id      NUMBER(2)                  NULL,
  show_in_series   NUMBER(1)                  NULL,
  calendar_id      RAW(32)                    NOT NULL,
  CONSTRAINT event_pk PRIMARY KEY (event_id),
  CONSTRAINT event_parent_fk FOREIGN KEY (parent_event_id) REFERENCES c##test.events (event_id) ON DELETE CASCADE,
  CONSTRAINT event_fk_calendar FOREIGN KEY (calendar_id) REFERENCES c##test.calendars (calendar_id) ON DELETE CASCADE
);

CREATE INDEX c##test.index_events ON c##test.events (
  deleted,
  parent_event_id,
  show_in_series,
  calendar_id,
  rec_type_id,
  start_time_utc,
  start_time_tz,
  end_time_utc,
  rec_end_time_utc
);

GRANT SELECT ON c##test.events TO c##test;

COMMIT;