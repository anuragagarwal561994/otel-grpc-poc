package org.example.client;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.xds.XdsChannelCredentials;
import org.aeonbits.owner.ConfigCache;
import org.example.ipservice.IPServiceGrpc;
import org.example.ipservice.Ipservice;

class IPServiceGrpcClient {
    private final IPServiceGrpc.IPServiceFutureStub futureStub;

    public IPServiceGrpcClient(String grpcTarget) {
        var channelCredentials = InsecureChannelCredentials.create();
        var xdsCredentials = XdsChannelCredentials.create(channelCredentials);
        var channel = Grpc.newChannelBuilder(grpcTarget, xdsCredentials).build();
        this.futureStub = IPServiceGrpc.newFutureStub(channel);
    }

    public static IPServiceGrpcClient create() {
        var config = ConfigCache.getOrCreate(GatewayConfig.class);
        var grpcTarget = config.grpcTarget();
        return new IPServiceGrpcClient(grpcTarget);
    }

    public ListenableFuture<Ipservice.IPResponse> getIpInfo() {
        return futureStub.getIPInfo(Ipservice.Empty.getDefaultInstance());
    }
}
