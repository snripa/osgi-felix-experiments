package org.rii.hello;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        HelloWorld.init();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        HelloWorld.destroy();
    }
}
