package com.wind.common.utils.validate;

import com.wind.common.constant.StatusConsts;
import com.wind.common.dto.ResponseDTO;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.*;
import java.util.List;
import java.util.Set;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/2/25 17:05
 **/
public class ValidateUtils {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 入参校验工具类 返回ResponseDTO
     *
     * @param obj
     * @return
     */
    public static ResponseDTO validate(Object obj) {
        ValidateResult validator = validator(obj);
        if (!validator.isSuccess()) {
            String msg = ArrayUtils.toString(validator.getErrMsg(), ",");
            return ResponseDTO.fail(StatusConsts.ERROR_PARAMS, msg);
        } else {
            return ResponseDTO.success();
        }
    }
    /**
     * 返回验证结果
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> ValidateResult validator(T object) {
        if (null == object) {
            return new ValidateResult(false, new String[]{"The object to be validated must not be null."});
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);
        int errSize = violations.size();

        String[] errMsg = new String[errSize];
        boolean result = true;
        if (errSize > 0) {
            int i = 0;
            for (ConstraintViolation<T> violation : violations) {
                errMsg[i] = violation.getMessage();
                i++;
            }
            result = false;
        }

        return new ValidateResult(result, errMsg);
    }

    /**
     * 返回验证结果
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> ValidateResult validator(List<T> list) {
        if (null == list) {
            return new ValidateResult(false,
                    new String[]{"The object to be validated must not be null."});
        }

        if (list.isEmpty()) {
            return new ValidateResult(true, new String[]{""});
        }

        ValidateResult validateResult = new ValidateResult(true, new String[]{""});
        for (T t : list) {
            validateResult = validator(t);
            if (!validateResult.isSuccess()) {
                break;
            }
        }

        return validateResult;
    }

    /**
     * 验证不通过抛出异常
     *
     * @param object
     * @param <T>
     */
    public static <T> void validateWithException(T object) throws ValidationException {
        if (null == object) {
            throw new IllegalArgumentException("验证对象为null");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * 验证不通过抛出异常
     *
     * @param list
     * @param <T>
     */
    public static <T> void validateWithException(List<T> list) throws ValidationException {
        if (null == list) {
            throw new IllegalArgumentException("验证对象为null");
        }

        for (T t : list) {
            Set<ConstraintViolation<T>> violations = validator.validate(t);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }
}
