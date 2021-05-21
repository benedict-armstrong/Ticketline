package at.ac.tuwien.sepm.groupphase.backend.endpoint.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomLayoutValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomLayoutConstraint {
    String message() default "Invalid Layout Matrix";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
