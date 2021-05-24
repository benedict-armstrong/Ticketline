package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressSpecificationBuilder {
    private final List<SearchCriteria> params;

    public AddressSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public AddressSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Address> build() {
        if (params.size() == 0) {
            return null;
        }

        List<AddressSpecification> specs = params.stream()
            .map(AddressSpecification::new)
            .collect(Collectors.toList());

        AddressSpecification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result.and(specs.get(i));
        }
        return result;
    }
}
