package me.lokic.dataloaderplus.core;

import lombok.Builder;
import lombok.Getter;
import org.dataloader.DataLoaderOptions;

/**
 * {@link DataLoaderTemplate} 需要的配置
 */
@Getter
@Builder
public class TemplateOptions {

    private static final DataLoaderOptions DEFAULT_OPTIONS = DataLoaderOptions.newOptions();
    public static final DataLoaderFactory DEFAULT_FACTORY = new DataLoaderFactory();

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;


    public TemplateOptions(DataLoaderOptions options, DataLoaderFactory factory) {
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
