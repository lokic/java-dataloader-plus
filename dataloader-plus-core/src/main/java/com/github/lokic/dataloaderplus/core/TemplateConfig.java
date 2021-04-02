package com.github.lokic.dataloaderplus.core;

import lombok.Builder;
import org.dataloader.DataLoaderOptions;

/**
 * {@link DataLoaderTemplate} 需要的配置
 */
@Builder
public class TemplateConfig {

    private static final DataLoaderOptions DEFAULT_OPTIONS = DataLoaderOptions.newOptions();
    private static final DataLoaderFactory DEFAULT_FACTORY = new DataLoaderFactory();


    public static final TemplateConfig DEFAULT = TemplateConfig.builder().build();

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;


    private TemplateConfig(DataLoaderOptions options, DataLoaderFactory factory) {
        this.options = options == null ? DEFAULT_OPTIONS : options;
        this.factory = factory == null ? DEFAULT_FACTORY : factory;
    }

    public DataLoaderOptions getOptions() {
        return options;
    }

    public DataLoaderFactory getFactory() {
        return factory;
    }
}
