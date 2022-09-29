package de.lehrcode.untier.model;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface AttachmentRepository extends PagingAndSortingRepository<Attachment, Long> {
    @Query("SELECT \"id\" FROM \"attachments\" WHERE \"checksum\" = :checksum")
    Optional<Long> findIdByChecksum(@Param("checksum") byte[] checksum);
}
