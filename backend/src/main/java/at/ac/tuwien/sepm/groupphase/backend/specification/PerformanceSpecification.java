package at.ac.tuwien.sepm.groupphase.backend.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

@AllArgsConstructor
public class PerformanceSpecification implements Specification<Performance> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Performance> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(criteriaBuilder.lower(
                    root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("+")) {
            return criteriaBuilder.between(root.get(criteria.getKey()), ((LocalDateTime) criteria.getValue()).minusHours(1), ((LocalDateTime) criteria.getValue()).plusHours(1));
        }

        return null;
    }
}
