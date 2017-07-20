package com.tairanchina.csp.dew.core.validation;

import com.ecfront.dew.common.$;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by 迹_Jason on 2017/7/19.
 * Validation of the CardID.
 */
public class CardIDValidator implements ConstraintValidator<CardID, String> {
    @Override
    public void initialize(CardID constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value))
            return true;
        if ($.field.validateIdNumber(value)) {
            return true;
        }
        return false;
    }
}
