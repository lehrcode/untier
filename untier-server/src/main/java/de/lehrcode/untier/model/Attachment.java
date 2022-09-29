package de.lehrcode.untier.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(setterPrefix = "with")
@Table("attachments")
public class Attachment {
    @Id
    @Column("id")
    private Long id;

    @ToString.Exclude
    @Column("bytes")
    private byte[] bytes;

    @Column("media_type")
    private String mediaType;

    @Column("checksum")
    private byte[] checksum;

    @Column("created")
    private OffsetDateTime created;
}
