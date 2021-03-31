package me.lokic.dataloaderplus.core.annotation;

import org.dataloader.BatchLoaderEnvironment;

import java.lang.annotation.*;

/**
 * key的上下文，一个方法最多只能有一个 @{@link KeyContext}
 *
 * <p>见：{@link BatchLoaderEnvironment#getKeyContextsList()} 和 {@link BatchLoaderEnvironment#getKeyContexts()}
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface KeyContext {
}
