package de.lehrcode.untier.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(setterPrefix = "with")
@Table("postings")
public class Posting {
    @Id
    @Column("id")
    private Long id;

    @Column("author")
    private String author;

    @Column("text")
    private String text;

    @Column("attachment_id")
    private Long attachmentId;

    @Column("published")
    private OffsetDateTime published;
}
