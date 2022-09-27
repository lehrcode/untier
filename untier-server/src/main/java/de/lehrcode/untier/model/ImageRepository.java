package de.lehrcode.untier.model;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
    @Query("SELECT \"id\" FROM \"images\" WHERE \"checksum\" = :checksum")
    Optional<Long> findIdByChecksum(@Param("checksum") byte[] checksum);
}
