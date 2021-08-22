package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistSpecificationBuilder {

    private final List<SearchCriteria> params;

    public ArtistSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public ArtistSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Artist> build() {
        if (params.size() == 0) {
            return null;
        }

        List<ArtistSpecification> specs = params.stream()
            .map(ArtistSpecification::new)
            .collect(Collectors.toList());

        ArtistSpecification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result.and(specs.get(i));
        }
        return result;
    }
}
