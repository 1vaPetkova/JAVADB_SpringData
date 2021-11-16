package com.example.java_db_09_exercise.util.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtilImpl implements ValidationUtil {

    private final Validator validator;

    public ValidationUtilImpl(Validator validator) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <E> Boolean isValid(E entity) {
        return validator.validate(entity).isEmpty();
    }

    @Override
    public <E> Set<ConstraintViolation<E>> getViolations(E entity) {
        return validator.validate(entity);
    }
}
