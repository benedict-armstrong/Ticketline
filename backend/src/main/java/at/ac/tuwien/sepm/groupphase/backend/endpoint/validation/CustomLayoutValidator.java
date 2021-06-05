package at.ac.tuwien.sepm.groupphase.backend.endpoint.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LayoutUnitDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CustomLayoutValidator implements ConstraintValidator<CustomLayoutConstraint, List<List<LayoutUnitDto>>> {

    @Override
    public void initialize(CustomLayoutConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<List<LayoutUnitDto>> lists, ConstraintValidatorContext constraintValidatorContext) {
        if (lists == null) {
            return false;
        }
        int width = lists.get(0).size();
        if (width < 2) {
            return false;
        }
        boolean isEmpty = true;
        for (List<LayoutUnitDto> row : lists) {
            if (row.size() != width) {
                return false;
            }
            if (isEmpty) {
                for (LayoutUnitDto lu: row) {
                    if (lu != null) {
                        isEmpty = false;
                        break;
                    }
                }
            }
        }
        return !isEmpty;
    }
}
