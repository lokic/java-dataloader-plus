package com.github.lokic.dataloaderplus.core;

import org.dataloader.DataLoaderRegistry;
import org.dataloader.registries.DispatchPredicate;
import org.dataloader.registries.ScheduledDataLoaderRegistry;

import java.time.Duration;

public enum RegistryFactoryType implements RegistryFactory {
    DEFAULT() {
        @Override
        public DataLoaderRegistry newRegistry() {
            return new DataLoaderRegistry();
        }
    },
    SCHEDULED() {
        @Override
        public DataLoaderRegistry newRegistry() {
            DispatchPredicate depthOrTimePredicate = DispatchPredicate
                    .dispatchIfDepthGreaterThan(500)
                    .or(DispatchPredicate.dispatchIfLongerThan(Duration.ofMillis(100)));

            return ScheduledDataLoaderRegistry.newScheduledRegistry()
                    .dispatchPredicate(depthOrTimePredicate)
                    .schedule(Duration.ofMillis(10))
                    .build();
        }
    }

}
