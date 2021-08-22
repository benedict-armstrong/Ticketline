package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventSpecificationBuilder {

    private final List<SearchCriteria> params;

    public EventSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public EventSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Event> build() {
        if (params.size() == 0) {
            return null;
        }

        List<EventSpecification> specs = params.stream()
            .map(EventSpecification::new)
            .collect(Collectors.toList());

        EventSpecification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result.and(specs.get(i));
        }
        return result;
    }
}
