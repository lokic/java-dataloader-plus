package com.github.lokic.dataloaderplus.spring;


import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;

public class DataLoaderAttribute {

    /**
     * @see DataLoadable#batchingEnabled()
     */
    private boolean batchingEnabled;

    /**
     * @see DataLoadable#cachingEnabled()
     */
    private boolean cachingEnabled;

    /**
     * @see DataLoadable#cachingExceptionsEnabled()
     */
    private boolean cachingExceptionsEnabled;

    /**
     * @see DataLoadable#maxBatchSize()
     */
    private int maxBatchSize;

    public boolean batchingEnabled() {
        return batchingEnabled;
    }

    public void setBatchingEnabled(boolean batchingEnabled) {
        this.batchingEnabled = batchingEnabled;
    }

    public boolean cachingEnabled() {
        return cachingEnabled;
    }

    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    public boolean cachingExceptionsEnabled() {
        return cachingExceptionsEnabled;
    }

    public void setCachingExceptionsEnabled(boolean cachingExceptionsEnabled) {
        this.cachingExceptionsEnabled = cachingExceptionsEnabled;
    }

    public int maxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }


}
