CREATE TABLE "attachments" (
    "id" BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
    "bytes" VARBINARY(1048576) NOT NULL,
    "media_type" VARCHAR(255) NOT NULL,
    "checksum" BINARY(32) NOT NULL,
    "created" TIMESTAMP(3) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT attachments_pk PRIMARY KEY ("id")
)

CREATE TABLE "postings" (
    "id" BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) NOT NULL,
    "author" VARCHAR(100) NOT NULL,
    "text" VARCHAR(400),
    "attachment_id" BIGINT,
    "published" TIMESTAMP(3) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT postings_pk PRIMARY KEY ("id"),
    CONSTRAINT postings_attachments_fk FOREIGN KEY ("attachment_id") REFERENCES "attachments"("id")
);
