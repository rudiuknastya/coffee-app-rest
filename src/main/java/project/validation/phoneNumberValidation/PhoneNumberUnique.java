package project.validation.phoneNumberValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
@Constraint(validatedBy = PhoneNumberUniqueValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberUnique {
    String id();
    String phoneNumber();
    String message() default "Такий номер телефону вже існує";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
