package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceSpecificationBuilder {

    private final List<SearchCriteria> params;

    public PerformanceSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public PerformanceSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Performance> build() {
        if (params.size() == 0) {
            return null;
        }

        List<PerformanceSpecification> specs = params.stream()
            .map(PerformanceSpecification::new)
            .collect(Collectors.toList());

        PerformanceSpecification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result.and(specs.get(i));
        }
        return result;
    }
}
