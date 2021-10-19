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

    /**
     * 当前线程设置ExDataLoaderRegistry
     *
     * @param initRegistry
     */
    private static void init(ExDataLoaderRegistry initRegistry) {
        CURRENT_HOLDER.set(initRegistry);
    }

    /**
     * 挂起当前ExDataLoaderRegistry，并返回挂起的ExDataLoaderRegistry
     *
     * @return
     */
    private static ExDataLoaderRegistry suspend() {
        ExDataLoaderRegistry suspendRegistry = CURRENT_HOLDER.get();
        CURRENT_HOLDER.remove();
        return suspendRegistry;
    }

    /**
     * 初始化ExDataLoaderRegistry，如果有当前活跃则挂起当前活跃的ExDataLoaderRegistry并初始化
     *
     * @param initRegistry
     * @return 如果有挂起的ExDataLoaderRegistry则返回挂起的ExDataLoaderRegistry，没有则返回null
     */
    public static ExDataLoaderRegistry initAndSuspend(ExDataLoaderRegistry initRegistry) {
        ExDataLoaderRegistry suspendedRegistry = null;
        if (RegistryManager.isActive()) {
            suspendedRegistry = RegistryManager.suspend();
        }
        RegistryManager.init(initRegistry);
        return suspendedRegistry;
    }


    /**
     * 唤起{@code suspendedRegistry}，作为当前活跃的ExDataLoaderRegistry
     *
     * @param suspendedRegistry
     */
    public static void resume(ExDataLoaderRegistry suspendedRegistry) {
        CURRENT_HOLDER.set(suspendedRegistry);
    }


    /**
     * 当前是否有活跃的ExDataLoaderRegistry
     *
     * @return
     */
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
