package com.wind.dao.buildsql.proxy;

import com.wind.dao.buildsql.config.BuildSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/12/17 11:31
 **/
@Slf4j
public class BuildClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private boolean addToConfig = true;

    private BuildSqlConfiguration buildSqlConfiguration;

    private Class<? extends BuildSqlMapperFactoryBean> mapperFactoryBeanClass = BuildSqlMapperFactoryBean.class;
    /**
     * 是否延迟加载
     */
    private boolean lazyInitialization;

    public BuildClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    /**
     * 注册过滤器
     */
    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        //使用注解
        if (this.annotationClass != null) {
            log.debug("---------------------扫描注解:{}-----------------------", this.annotationClass.getName());
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        //  只有集成了指定接口的才会被扫描到
        if (this.markerInterface != null) {
            log.debug("---------------------扫描顶级接口:{}-----------------------", this.markerInterface);
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }
        if (acceptAllInterfaces) {
            log.debug("---------------------扫描纯接口------------------------");
            // default include filter that accepts all classes
            addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                return true;
            });
        }
        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn(" buildsql No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;

        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.mapperFactoryBeanClass);
            definition.getPropertyValues().add("addToConfig", this.addToConfig);
            log.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' mapperInterface");
            //definition.getPropertyValues().add("buildSqlConfiguration",new RuntimeBeanReference(this.buildSqlConfiguration.getClass().getName()));
            definition.getPropertyValues().add("buildSqlConfiguration", this.buildSqlConfiguration);
            log.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            log.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface" + ". Bean already defined with the same name!");
            return false;
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<?> getMarkerInterface() {
        return markerInterface;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public boolean isAddToConfig() {
        return addToConfig;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public boolean isLazyInitialization() {
        return lazyInitialization;
    }

    public void setLazyInitialization(boolean lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
    }

    public BuildSqlConfiguration getBuildSqlConfiguration() {
        return buildSqlConfiguration;
    }

    public void setBuildSqlConfiguration(BuildSqlConfiguration buildSqlConfiguration) {
        this.buildSqlConfiguration = buildSqlConfiguration;
    }

    public Class<? extends BuildSqlMapperFactoryBean> getMapperFactoryBeanClass() {
        return mapperFactoryBeanClass;
    }

    public void setMapperFactoryBeanClass(Class<? extends BuildSqlMapperFactoryBean> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass;
    }


}
