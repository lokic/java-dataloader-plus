package com.github.lokic.dataloaderplus.spring.annotation;

import com.github.lokic.dataloaderplus.spring.DataLoaderConfigurationSelector;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(DataLoaderConfigurationSelector.class)
public @interface EnableDataLoader {

    String[] basePackages() default {};
}
