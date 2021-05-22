package at.ac.tuwien.sepm.groupphase.backend.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventSearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
