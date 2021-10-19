package com.github.lokic.dataloaderplus.spring;


import com.github.lokic.dataloaderplus.core.Propagation;
import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;

public class DataLoadableAttribute {

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

    /**
     * @see DataLoadable#propagation()
     */
    private Propagation propagation;

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

    public Propagation propagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public static DataLoadableAttribute parseAttribute(DataLoadable dataLoadable) {
        DataLoadableAttribute attribute = new DataLoadableAttribute();
        attribute.setBatchingEnabled(dataLoadable.batchingEnabled());
        attribute.setCachingEnabled(dataLoadable.cachingEnabled());
        attribute.setCachingExceptionsEnabled(dataLoadable.cachingExceptionsEnabled());
        attribute.setMaxBatchSize(dataLoadable.maxBatchSize());
        attribute.setPropagation(dataLoadable.propagation());
        return attribute;
    }

}
