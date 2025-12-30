package com.xiaowei.framework.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author: 魏玉石
 * @data: 2025/12/31
 * @description: 手机号校验
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,String> {

    /**
     * @phoneNumber: 需要验证的字符串，即被注解的属性值。
     * @constraintValidatorContext: 提供了一些校验的上下文信息，通常用来设置错误消息等。
     * @return: 验证结果，true 表示验证通过，false 表示验证失败。
     */
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber != null && phoneNumber.matches("\\d{11}")){
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "手机号格式不正确,需为11位数字"
        ).addConstraintViolation();

        return false;
    }
}
