package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Finds news entries.
     *
     * @param limit the amount of news entries to be retrieved
     * @param offset the maximum ID the news entries must have
     * @return ordered list of satisfying news entries
     */
    @Query(value = "SELECT * FROM News n WHERE n.id < ?2 ORDER BY n.published_at DESC LIMIT ?1", nativeQuery = true)
    List<News> getAll(Long limit, Long offset);

    /**
     * Find one news by id.
     *
     * @param id of the news to find
     * @return news with the id.
     */
    News findOneById(Long id);
}
