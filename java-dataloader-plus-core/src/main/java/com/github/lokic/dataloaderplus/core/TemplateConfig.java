package com.github.lokic.dataloaderplus.core;

import lombok.NonNull;
import org.dataloader.DataLoaderOptions;

import java.util.Objects;

/**
 * {@link DataLoaderTemplate} 需要的配置
 */
public class TemplateConfig {

    private static final DataLoaderOptions DEFAULT_OPTIONS = DataLoaderOptions.newOptions();
    private static final DataLoaderFactory DEFAULT_FACTORY = new DataLoaderFactory();

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;
    private final Propagation propagation;

    public TemplateConfig(DataLoaderOptions options, DataLoaderFactory factory, Propagation propagation) {
        this.options = options == null ? DEFAULT_OPTIONS : options;
        this.factory = factory == null ? DEFAULT_FACTORY : factory;
        this.propagation = Objects.requireNonNull(propagation);
    }

    public DataLoaderOptions getOptions() {
        return options;
    }

    public DataLoaderFactory getFactory() {
        return factory;
    }

    public Propagation getPropagation() {
        return propagation;
    }
}
