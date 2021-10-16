package com.github.lokic.dataloaderplus.core;

/**
 * 以线程绑定的形式获取 {@link ExDataLoaderRegistry}
 */
public class RegistryManager {

    public static final ThreadLocal<ExDataLoaderRegistry> CURRENT_HOLDER = new ThreadLocal<>();

    /**
     * 获取当前线程的ExDataLoaderRegistry
     *
     * @return
     */
    public static ExDataLoaderRegistry getRegistry() {
        return CURRENT_HOLDER.get();
    }

    public static ExDataLoaderRegistry suspend() {
        ExDataLoaderRegistry suspendRegistry = CURRENT_HOLDER.get();
        CURRENT_HOLDER.remove();
        return suspendRegistry;
    }

    public static void resume(ExDataLoaderRegistry suspendedRegistry) {
        CURRENT_HOLDER.set(suspendedRegistry);
    }

    /**
     * 当前线程设置ExDataLoaderRegistry
     *
     * @param registry
     */
    public static void init(ExDataLoaderRegistry registry) {
        CURRENT_HOLDER.set(registry);
    }

    public static boolean isActive() {
        return CURRENT_HOLDER.get() != null;
    }

    /**
     * 清除当前线程的ExDataLoaderRegistry
     */
    public static void clear() {
        CURRENT_HOLDER.get().close();
        CURRENT_HOLDER.remove();
    }

    /**
     * 针对当前线程的ExDataLoaderRegistry
     * 如果存在则调用 {@link ExDataLoaderRegistry#dispatchAll()}, 不存在则忽略该请求
     */
    public static void tryDispatchAll() {
        ExDataLoaderRegistry registry = getRegistry();
        if (registry != null) {
            registry.dispatchAll();
        }
    }

    public static void tryDispatchAll(ExDataLoaderRegistry registry) {
        if (registry != null) {
            registry.dispatchAll();
        }
    }


}
