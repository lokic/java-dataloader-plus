package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.TemplateConfig;
import org.dataloader.DataLoaderOptions;

public class DataLoaderTemplateManager {

    private final DataLoaderOptions defaultOptions;
    private final DataLoaderFactory defaultFactory;

    public DataLoaderTemplateManager(DataLoaderOptions defaultOptions, DataLoaderFactory defaultFactory) {
        this.defaultOptions = defaultOptions;
        this.defaultFactory = defaultFactory;
    }

    public DataLoaderTemplate newTemplate() {
        TemplateConfig config = TemplateConfig.builder()
                .options(defaultOptions)
                .factory(defaultFactory)
                .build();
        return new DataLoaderTemplate(config);
    }

    public DataLoaderTemplate newTemplate(DataLoaderOptions options) {
        TemplateConfig config = TemplateConfig.builder()
                .options(options)
                .factory(defaultFactory)
                .build();
        return new DataLoaderTemplate(config);
    }
}
