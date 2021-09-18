package com.github.lokic.dataloaderplus.spring.annotation;

import com.github.lokic.dataloaderplus.spring.DataLoaderAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DataLoaderAutoConfiguration.class)
public @interface EnableDataLoader {

}
