package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Finds news entries.
     *
     * @param pageable a Pageable page request
     * @return ordered list of satisfying news entries
     */
    Page<News> findAllByOrderByPublishedAtDesc(Pageable pageable);

    /**
     * Find one news by id.
     *
     * @param id of the news to find
     * @return news with the id.
     */
    News findOneById(Long id);

}
