package com.wind.dao.buildsql.proxy;

import com.wind.dao.buildsql.config.BuildSqlConfiguration;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/16 19:54
 **/
public class BuildSqlMapperProxy<T>  implements InvocationHandler, Serializable {

    private final Map<Method, BuildSqlMapperMethod> methodCache;
    private final Class<T> mapperInterface;
    private final BuildSqlConfiguration configuration;
    private static final Constructor<MethodHandles.Lookup> lookupConstructor;
    private static final Method privateLookupInMethod;
    private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
            | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

    static {
        Method privateLookupIn;
        try {
            privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
        } catch (NoSuchMethodException e) {
            privateLookupIn = null;
        }
        privateLookupInMethod = privateLookupIn;

        Constructor<MethodHandles.Lookup> lookup = null;
        if (privateLookupInMethod == null) {
            // JDK 1.8
            try {
                lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                lookup.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                        "There is neither 'privateLookupIn(Class, Lookup)' nor 'Lookup(Class, int)' method in java.lang.invoke.MethodHandles.",
                        e);
            } catch (Throwable t) {
                lookup = null;
            }
        }
        lookupConstructor = lookup;
    }

    public BuildSqlMapperProxy(Class<T> mapperInterface, Map<Method, BuildSqlMapperMethod> methodCache, BuildSqlConfiguration configuration) {
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
        this.configuration = configuration;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (method.isDefault()) {
                invokeDefaultMethodJava8(proxy, method, args);
            }
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
        final BuildSqlMapperMethod mapperMethod = cachedMapperMethod(method);
        return mapperMethod.execute(args);
    }


    private BuildSqlMapperMethod cachedMapperMethod(Method method) {
        return methodCache.computeIfAbsent(method,
                k -> new BuildSqlMapperMethod(mapperInterface, method, configuration));
    }

    private Object invokeDefaultMethodJava8(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        return lookupConstructor.newInstance(declaringClass, ALLOWED_MODES).unreflectSpecial(method, declaringClass)
                .bindTo(proxy).invokeWithArguments(args);
    }
}
