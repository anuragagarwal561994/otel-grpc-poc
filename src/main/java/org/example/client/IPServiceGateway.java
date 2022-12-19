package org.example.client;

import com.google.inject.servlet.GuiceFilter;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

@Slf4j
public class IPServiceGateway {
    private static final String DEFAULT_SERVLET = "DEFAULT";
    private static final String CONTEXT_PATH = "/ipservice";
    private static final String DISPLAY_NAME = "ipservice";

    private static void setConnectorAttributes(Connector connector) {
        connector.setProperty("maxThreads", "200");
        connector.setProperty("acceptCount", "20");
        connector.setProperty("minSpareThreads", "100");
        connector.addUpgradeProtocol(new Http2Protocol());
        connector.setProperty("keepAliveTimeout", "-1");
        connector.setProperty("maxKeepAliveRequests", "-1");
        connector.setProperty("connectionTimeout", "20000");
    }

    public static void main(String[] args) throws LifecycleException {
        int port = 8080;
        String webappDirLocation = ".";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        Connector connector = tomcat.getConnector();
        setConnectorAttributes(connector);
        String absolutePath = new File(webappDirLocation).getAbsolutePath();
        Context context = tomcat.addContext(CONTEXT_PATH, absolutePath);
        Tomcat.addServlet(context, DEFAULT_SERVLET, new DefaultServlet());
        context.addServletMappingDecoded("/", DEFAULT_SERVLET);
        context.setDisplayName(DISPLAY_NAME);
        context.addLifecycleListener(new Tomcat.FixContextListener());
        context.addApplicationListener(ApplicationListener.class.getName());
        setupGuiceFilter(context);
        tomcat.start();
        log.info("Started Tomcat Server on port: {}", port);
        tomcat.getServer().await();
    }

    private static void setupGuiceFilter(Context context) {
        String filterName = "guice";
        String allRoutes = "/*";

        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(new GuiceFilter());
        filterDef.setFilterName(filterName);
        context.addFilterDef(filterDef);
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(filterName);
        filterMap.addURLPattern(allRoutes);
        context.addFilterMap(filterMap);
    }
}
