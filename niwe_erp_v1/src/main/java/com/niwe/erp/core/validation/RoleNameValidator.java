package com.niwe.erp.core.validation;

import com.niwe.erp.core.util.annotation.ValidRoleName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleNameValidator implements ConstraintValidator<ValidRoleName, String> {
    
    @Override
    public void initialize(ValidRoleName constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext context) {
        if (roleName == null) {
            return false;
        }
        return roleName.startsWith("ROLE_");
    }
}