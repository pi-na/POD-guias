package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.health.HealthServiceGrpc;
import io.grpc.ManagedChannel;

public class HealthServiceClient {
    private final ar.edu.itba.pod.grpc.health.HealthServiceGrpc.HealthServiceBlockingStub blockingStub;

    public HealthServiceClient(final ManagedChannel channel) {
        this.blockingStub = HealthServiceGrpc.newBlockingStub(channel);
    }

    public String callPing(){
        final ar.edu.itba.pod.grpc.health.PingRequest request = ar.edu.itba.pod.grpc.health.PingRequest.newBuilder().build();
        final ar.edu.itba.pod.grpc.health.PingResponse response = blockingStub.ping(request);
        return response.getMessage();
    }
}
