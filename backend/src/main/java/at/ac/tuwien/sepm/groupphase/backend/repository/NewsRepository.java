package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find all news entries.
     *
     * @return ordered list of all news entries
     */
    @Query(value = "SELECT * FROM News n ORDER BY n.published_at LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<News> getAll(Long limit, Long offset);

}
