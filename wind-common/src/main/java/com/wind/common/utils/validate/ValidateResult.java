package com.wind.common.utils.validate;

import java.util.Arrays;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/2/25 17:03
 **/
public class ValidateResult {
    /**
     * 校验结果
     */
    private boolean result;

    /**
     * 错误信息
     */
    private String[] errMsg;

    public ValidateResult(boolean result, String[] errMsg) {
        this.result = result;
        this.errMsg = errMsg;
    }


    public boolean isSuccess() {
        return result;
    }

    public String[] getErrMsg() {
        return errMsg;
    }


    @Override
    public String toString() {
        return Arrays.toString(errMsg);
    }
}
