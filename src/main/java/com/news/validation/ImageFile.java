package com.news.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFile {
    String message() default "Only JPEG images are allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 