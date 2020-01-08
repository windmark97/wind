package com.wind.common.utils.amount;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 金额处理
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/8 13:08
 **/
public final class AmountUtil {

    /**
     * 以[分]为单位的正则
     */
    private static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 以[元]为单位的正则 验证Long型
     */
    private static final String CURRENCY_YUAN_REGEX_LONG = "^(([0-9]|([1-9][0-9]{0,15}))((.[0-9]{1,2})?))$";
    /**
     * 以[元]为单位的正则 验证Int型
     */
    private static final String CURRENCY_YUAN_REGEX_INT = "^(([0-9]|([1-9][0-9]{0,7}))((.[0-9]{1,2})?))$";

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amountStr 金额字符串满足[xx.xx](CURRENCY_FEN_REGEX)
     * @return BigDecimal
     * @throws RuntimeException 运行时金额格式错误
     */
    public static BigDecimal fen2Yuan(String amountStr) {
        if (Objects.isNull(amountStr)){
            amountStr = "0";
        }
        if (!amountStr.matches(CURRENCY_FEN_REGEX)) {
            throw new RuntimeException("金额格式错误|" + amountStr + "|不满足正则[" + CURRENCY_FEN_REGEX + "]");
        }
        return BigDecimal.valueOf(Long.valueOf(amountStr), 2);
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount 金额字符串满足[xx.xx](CURRENCY_FEN_REGEX)
     * @return BigDecimal
     */
    public static BigDecimal fen2Yuan(int amount) {
        if (Objects.isNull(amount)){
            amount = 0;
        }
        return BigDecimal.valueOf(amount, 2);
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount 金额字符串满足[xx.xx](CURRENCY_FEN_REGEX)
     * @return BigDecimal
     */
    public static BigDecimal fen2Yuan(long amount) {
        if (Objects.isNull(amount)){
            amount = 0;
        }
        return BigDecimal.valueOf(amount, 2);
    }

    /**
     * 将元为单位的参数转换为分 , 只对小数点前2位支持
     *
     * @param amountStr 金额字符串满足[xx.xx](CURRENCY_YUAN_REGEX_LONG)
     * @return int
     * @throws RuntimeException 运行时金额格式错误 ArithmeticException 算术异常（精度丢失风险需要四舍五入/溢出）
     */
    public static long yuan2FenLong(String amountStr) {
        return yuan2Fen(amountStr).longValueExact();
    }
    /**
     * 将元为单位的参数转换为分 , 只对小数点前2位支持
     *
     * @param amountStr 金额字符串满足[xx.xx](CURRENCY_YUAN_REGEX_LONG)
     * @return int
     * @throws RuntimeException 运行时金额格式错误/ ArithmeticException 算术异常（溢出）
     */
    public static int yuan2FenInt(String amountStr) {
        return yuan2Fen(amountStr).intValueExact();
    }

    public static BigDecimal yuan2Fen(String amountStr){
        if (Objects.isNull(amountStr)){
            amountStr = "0";
        }
        if (!amountStr.matches(CURRENCY_YUAN_REGEX_INT)) {
            throw new RuntimeException("金额格式错误|" + amountStr + "|不满足正则[" + CURRENCY_YUAN_REGEX_INT + "]");
        }
        BigDecimal fenBd = new BigDecimal(amountStr).multiply(new BigDecimal(100));
        fenBd = fenBd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return fenBd;
    }

    public static void main(String[] args) {
        System.out.println(AmountUtil.fen2Yuan(null));
        System.out.println(AmountUtil.fen2Yuan("123212232132"));
        System.out.println(AmountUtil.yuan2FenLong("1123321223.00"));
        System.out.println(AmountUtil.yuan2FenInt("1123323.00"));
    }
}
