package org.example.client;

import com.google.inject.Inject;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigCache;
import org.example.ipservice.Ipservice;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class IPServiceGrpcServlet extends HttpServlet {
    private final IPServiceGrpcClient client;
    private final JsonFormat.Printer printer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var config = ConfigCache.getOrCreate(GatewayConfig.class);
        var timeout = config.requestTimeout();
        var waitMs = timeout.toMillis();
        try {
            Ipservice.IPResponse ipResponse = client.getIpInfo().get(waitMs, TimeUnit.MILLISECONDS);
            printer.appendTo(ipResponse, resp.getWriter());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error executing call", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (TimeoutException e) {
            log.error("Timeout executing", e);
            resp.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
        }
    }
}
