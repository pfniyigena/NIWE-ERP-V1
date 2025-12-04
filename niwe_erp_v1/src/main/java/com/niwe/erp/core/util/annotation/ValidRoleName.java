package com.niwe.erp.core.util.annotation;
 
 
import java.lang.annotation.*;

import com.niwe.erp.core.validation.RoleNameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = RoleNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleName {
    String message() default "{role.name.validation}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
