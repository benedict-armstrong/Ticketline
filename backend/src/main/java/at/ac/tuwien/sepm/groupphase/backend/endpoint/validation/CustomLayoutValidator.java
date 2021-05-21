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
        int width = lists.get(0).size();
        for (List<LayoutUnitDto> row: lists) {
            if (row.size() != width) {
                return false;
            }
        }
        return true;
    }
}
