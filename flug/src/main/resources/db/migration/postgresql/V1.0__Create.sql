CREATE TABLE IF NOT EXISTS flugzeug (
                                        id UUID PRIMARY KEY USING INDEX TABLESPACE flugspace,
                                        model VARCHAR(40) NOT NULL
) TABLESPACE flugspace;

CREATE INDEX IF NOT EXISTS flugzeug_model_idx ON flugzeug(model) TABLESPACE flugspace;


CREATE TABLE IF NOT EXISTS flug (
                                    id UUID PRIMARY KEY USING INDEX TABLESPACE flugspace,
                                    hotel_id UUID NOT NULL,
                                    version INTEGER NOT NULL DEFAULT 0,
                                    start_flughafen VARCHAR(40) NOT NULL,
                                    ziel_flughafen VARCHAR(40) NOT NULL,
                                    flugzeug_id UUID NOT NULL REFERENCES flugzeug(id),
                                    status VARCHAR(32),
                                    erzeugt TIMESTAMP NOT NULL,
                                    aktualisiert TIMESTAMP NOT NULL
) TABLESPACE flugspace;

CREATE INDEX IF NOT EXISTS flug_flughafen_idx ON flug(start_flughafen) TABLESPACE flugspace;


CREATE TABLE IF NOT EXISTS buchung (
                                       id UUId PRIMARY KEY USING INDEX TABLESPACE flugspace,
                                       nachname VARCHAR(40) NOT NULL,
                                       flug_id UUID REFERENCES flug(id),
                                       idx INTEGER NOT NULL DEFAULT 0,
                                       zeitpunkt  TIMESTAMP
) TABLESPACE flugspace;

CREATE INDEX IF NOT EXISTS buchung_flug_id_idx ON buchung(flug_id) TABLESPACE flugspace;

