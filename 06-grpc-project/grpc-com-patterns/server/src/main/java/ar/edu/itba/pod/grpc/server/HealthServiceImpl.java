package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.health.HealthServiceGrpc;
import ar.edu.itba.pod.grpc.health.PingRequest;
import ar.edu.itba.pod.grpc.health.PingResponse;
import io.grpc.stub.StreamObserver;

public class HealthServiceImpl extends HealthServiceGrpc.HealthServiceImplBase {
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        // Recibe una PingRequest y crea una PingResponse
        PingResponse response = PingResponse.newBuilder().setMessage("mensajito").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
