package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.TemplateConfig;
import org.dataloader.DataLoaderOptions;

import java.util.Objects;

public class DataLoaderTemplateFactory {
    private final DataLoaderOptions defaultOptions;
    private final DataLoaderFactory defaultFactory;

    public DataLoaderTemplateFactory(DataLoaderOptions defaultOptions, DataLoaderFactory defaultFactory) {
        this.defaultOptions = defaultOptions;
        this.defaultFactory = defaultFactory;
    }

    public DataLoaderTemplate createTemplate(DataLoadableAttribute attribute) {
        TemplateConfig config = new TemplateConfig(parseOptions(attribute), defaultFactory, attribute.propagation());
        return new DataLoaderTemplate(config);
    }

    private DataLoaderOptions parseOptions(DataLoadableAttribute attribute) {
        return isEqualsDefaultOptions(attribute) ? defaultOptions : createOptions(attribute);
    }

    private DataLoaderOptions createOptions(DataLoadableAttribute attribute) {
        DataLoaderOptions options = new DataLoaderOptions();
        options.setBatchingEnabled(attribute.batchingEnabled());
        options.setCachingEnabled(attribute.cachingEnabled());
        options.setCachingExceptionsEnabled(attribute.cachingExceptionsEnabled());
        options.setMaxBatchSize(attribute.maxBatchSize());
        return options;
    }


    private boolean isEqualsDefaultOptions(DataLoadableAttribute attribute) {
        return Objects.equals(defaultOptions.batchingEnabled(), attribute.batchingEnabled())
                && Objects.equals(defaultOptions.cachingEnabled(), attribute.cachingEnabled())
                && Objects.equals(defaultOptions.cachingExceptionsEnabled(), attribute.cachingExceptionsEnabled())
                && Objects.equals(defaultOptions.maxBatchSize(), attribute.maxBatchSize());
    }
}
