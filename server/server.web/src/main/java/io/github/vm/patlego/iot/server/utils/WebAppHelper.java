package io.github.vm.patlego.iot.server.utils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class WebAppHelper {

    private WebAppHelper() {
        throw new IllegalStateException("Utility class");
      }
    
    public static <T> T getService(BundleContext context, Class<T> clazz) {
        ServiceReference<T> reference = context.getServiceReference(clazz);
        return context.getService(reference);
    }
}
