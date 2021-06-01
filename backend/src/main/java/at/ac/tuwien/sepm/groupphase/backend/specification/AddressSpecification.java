package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class AddressSpecification implements Specification<Address> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Address> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(criteriaBuilder.lower(
                    root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            }
        } else if (criteria.getOperation().equalsIgnoreCase("?")) {
            if (root.get(criteria.getKey()).getJavaType() == boolean.class) {
                return criteriaBuilder.isTrue(root.get(criteria.getKey()));
            }
        }

        return null;
    }

}
