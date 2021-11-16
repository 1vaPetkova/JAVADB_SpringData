package com.example.java_db_06_exercise.model.enitities.annotations;

import javax.validation.ConstraintValidatorContext;

public final class AnnotationsUtil {

    private AnnotationsUtil() {
    }

    public static void setErrorMessage(ConstraintValidatorContext validatorContext, String errorMessage) {
        validatorContext.disableDefaultConstraintViolation();
        validatorContext.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
    }
}
