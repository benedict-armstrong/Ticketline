package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventSpecificationBuilder {

    private final List<EventSearchCriteria> params;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public EventSpecificationBuilder() {
        params = new ArrayList<EventSearchCriteria>();
    }

    public EventSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new EventSearchCriteria(key, operation, value));
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
