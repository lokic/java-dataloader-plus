package me.lokic.dataloaderplus.core.proxy;

import java.lang.reflect.Proxy;

public class InterfaceProxy {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> innerInterface) {
        ClassLoader classLoader = innerInterface.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{innerInterface};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, ServiceInvocationHandler.INSTANCE);
    }

}
