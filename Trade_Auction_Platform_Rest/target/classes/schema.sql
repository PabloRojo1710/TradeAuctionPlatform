
CREATE TABLE account (
    profile_username  VARCHAR2(20) NOT NULL,
    id                VARCHAR2(40) NOT NULL,
    euros             FLOAT NOT NULL,
    bitcoins          FLOAT NOT NULL
);

CREATE UNIQUE INDEX account__idx ON
    account (
        profile_username
    ASC );

ALTER TABLE account ADD CONSTRAINT account_pk PRIMARY KEY ( id );

CREATE TABLE auction (
    profile_username  VARCHAR2(20) NOT NULL,
    id                VARCHAR2(50) NOT NULL,
    bitcoins          FLOAT NOT NULL,
    startingdate      TIMESTAMP WITH TIME ZONE NOT NULL,
    endingdate        TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE auction ADD CONSTRAINT auction_pk PRIMARY KEY ( id );

CREATE TABLE bid (
    "date"            TIMESTAMP WITH TIME ZONE NOT NULL,
    priceeuros        FLOAT NOT NULL,
    numberbitcoins    FLOAT NOT NULL,
    bitcoinsearned    FLOAT NOT NULL,
    profile_username  VARCHAR2(20) NOT NULL,
    auction_id        VARCHAR2(50) NOT NULL,
    comission         FLOAT NOT NULL
);

ALTER TABLE bid
    ADD CONSTRAINT bid_pk PRIMARY KEY ( profile_username,
                                        "date",
                                        auction_id );

CREATE TABLE profile (
    username    VARCHAR2(20) NOT NULL,
    password    VARCHAR2(50) NOT NULL,
    email       VARCHAR2(50) NOT NULL,
    type        VARCHAR2(10) NOT NULL,
    account_id  VARCHAR2(40) NOT NULL
);

CREATE UNIQUE INDEX profile__idx ON
    profile (
        account_id
    ASC );

ALTER TABLE profile ADD CONSTRAINT profile_pk PRIMARY KEY ( username );

ALTER TABLE profile ADD CONSTRAINT profile_email_un UNIQUE ( email );

CREATE TABLE purchase (
    profile_username  VARCHAR2(20) NOT NULL,
    id                VARCHAR2(50) NOT NULL,
    "date"            TIMESTAMP WITH TIME ZONE NOT NULL,
    numbitcoins       FLOAT NOT NULL,
    numeuros          FLOAT NOT NULL,
    comission         FLOAT NOT NULL
);

ALTER TABLE purchase ADD CONSTRAINT purchase_pk PRIMARY KEY ( id );

ALTER TABLE account
    ADD CONSTRAINT account_profile_fk FOREIGN KEY ( profile_username )
        REFERENCES profile ( username );

ALTER TABLE auction
    ADD CONSTRAINT auction_profile_fk FOREIGN KEY ( profile_username )
        REFERENCES profile ( username );

ALTER TABLE bid
    ADD CONSTRAINT bid_auction_fk FOREIGN KEY ( auction_id )
        REFERENCES auction ( id );

ALTER TABLE bid
    ADD CONSTRAINT bid_profile_fk FOREIGN KEY ( profile_username )
        REFERENCES profile ( username );

ALTER TABLE profile
    ADD CONSTRAINT profile_account_fk FOREIGN KEY ( account_id )
        REFERENCES account ( id );

ALTER TABLE purchase
    ADD CONSTRAINT purchase_profile_fk FOREIGN KEY ( profile_username )
        REFERENCES profile ( username );


