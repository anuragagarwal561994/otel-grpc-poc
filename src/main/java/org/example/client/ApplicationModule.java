package org.example.client;

import com.google.gson.Gson;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.google.protobuf.util.JsonFormat;

public class ApplicationModule extends ServletModule {
    @Override
    protected void configureServlets() {
        binder().disableCircularProxies();
        binder().requireExplicitBindings();
        binder().requireExactBindingAnnotations();
        binder().requireAtInjectOnConstructors();

        bind(IPServiceGrpcServlet.class).in(Singleton.class);
        serve("/grpc").with(IPServiceGrpcServlet.class);
    }

    @Provides
    @Singleton
    private IPServiceGrpcClient grpcClient() {
        return IPServiceGrpcClient.create();
    }

    @Provides
    @Singleton
    private Gson gson() {
        return new Gson();
    }

    @Provides
    @Singleton
    private JsonFormat.Printer jsonFormatPrinter() {
        return JsonFormat.printer().omittingInsignificantWhitespace();
    }
}
