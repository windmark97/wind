package com.wind.dao.buildsql.annotation;

import java.lang.annotation.*;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/18 11:50
 **/
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface BuildSql {


}
