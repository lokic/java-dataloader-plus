package me.lokic.dataloaderplus.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({DataLoaderServiceRegistrar.class})
public @interface EnableDataLoader {

}
