package com.github.lokic.dataloaderplus.core;


/**
 * 以线程绑定的形式获取 {@link ExDataLoaderRegistry}
 */
public class RegistryHolder {

    public static final ThreadLocal<ExDataLoaderRegistry> HOLDER = new ThreadLocal<>();

    /**
     * 获取当前线程的CustomizeDataLoaderRegistry
     *
     * @return
     */
    public static ExDataLoaderRegistry getRegistry() {
        return HOLDER.get();
    }

    /**
     * 当前线程设置CustomizeDataLoaderRegistry
     *
     * @param registry
     */
    public static void setRegistry(ExDataLoaderRegistry registry) {
        HOLDER.set(registry);
    }

    /**
     * 针对当前线程的CustomizeDataLoaderRegistry，
     * 如果存在则调用 {@link ExDataLoaderRegistry#dispatchAll()}, 不存在则忽略该请求
     */
    public static void tryDispatchAll() {
        ExDataLoaderRegistry registry = getRegistry();
        if (registry != null) {
            registry.dispatchAll();
        }
    }

    /**
     * 清除当前线程的CustomizeDataLoaderRegistry
     */
    public static void clear() {
        HOLDER.remove();
    }

}
