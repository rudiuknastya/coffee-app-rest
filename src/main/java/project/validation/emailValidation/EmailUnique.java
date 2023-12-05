package project.validation.emailValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {
    String id();
    String email();
    String message() default "Така пошта вже існує";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
