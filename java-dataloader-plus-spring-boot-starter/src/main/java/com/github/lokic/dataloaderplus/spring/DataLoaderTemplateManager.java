package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.TemplateConfig;
import org.dataloader.DataLoaderOptions;

import java.util.Objects;

public class DataLoaderTemplateManager {
    private final DataLoaderOptions defaultOptions;
    private final DataLoaderFactory defaultFactory;

    public DataLoaderTemplateManager(DataLoaderOptions defaultOptions, DataLoaderFactory defaultFactory) {
        this.defaultOptions = defaultOptions;
        this.defaultFactory = defaultFactory;
    }

    public DataLoaderTemplate newTemplate(DataLoaderAttribute attribute) {
        TemplateConfig config = TemplateConfig.builder()
                .options(parseOptions(attribute))
                .factory(defaultFactory)
                .build();
        return new DataLoaderTemplate(config);
    }

    private DataLoaderOptions parseOptions(DataLoaderAttribute attribute) {
        return isEqualsDataLoaderAttribute(attribute) ? defaultOptions : createOptions(attribute);
    }

    private DataLoaderOptions createOptions(DataLoaderAttribute attribute) {
        DataLoaderOptions options = new DataLoaderOptions();
        options.setBatchingEnabled(attribute.batchingEnabled());
        options.setCachingEnabled(attribute.cachingEnabled());
        options.setCachingExceptionsEnabled(attribute.cachingExceptionsEnabled());
        options.setMaxBatchSize(attribute.maxBatchSize());
        return options;
    }


    private boolean isEqualsDataLoaderAttribute(DataLoaderAttribute attribute) {
        return Objects.equals(defaultOptions.batchingEnabled(), attribute.batchingEnabled())
                && Objects.equals(defaultOptions.cachingEnabled(), attribute.cachingEnabled())
                && Objects.equals(defaultOptions.cachingExceptionsEnabled(), attribute.cachingExceptionsEnabled())
                && Objects.equals(defaultOptions.maxBatchSize(), attribute.maxBatchSize());
    }
}
