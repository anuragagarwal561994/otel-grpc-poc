package org.example.server;

import io.grpc.InsecureServerCredentials;
import io.grpc.stub.StreamObserver;
import io.grpc.xds.XdsServerBuilder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.ipservice.IPServiceGrpc;
import org.example.ipservice.Ipservice;
import org.example.service.FakeIpResponseGenerator;

@Slf4j
public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 9090;
        var credentials = InsecureServerCredentials.create();
        var serverBuilder = XdsServerBuilder.forPort(port, credentials);
        serverBuilder.addService(new IPServiceGrpcImpl());
        var grpcServer = serverBuilder.build();
        grpcServer.start();
        log.info("Started GRPC Server on port: {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                grpcServer.shutdown().awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
        grpcServer.awaitTermination();
    }

    private static class IPServiceGrpcImpl extends IPServiceGrpc.IPServiceImplBase {
        private final FakeIpResponseGenerator generator = new FakeIpResponseGenerator();

        @Override
        public void getIPInfo(Ipservice.Empty request,
            StreamObserver<Ipservice.IPResponse> responseObserver) {
            responseObserver.onNext(generator.generateResponse());
            responseObserver.onCompleted();
        }
    }
}
