package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.GreeterGrpc;
import ar.edu.itba.pod.grpc.HelloReply;
import ar.edu.itba.pod.grpc.HelloRequest;
import ar.edu.itba.pod.grpc.GreeterGrpc.GreeterImplBase;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class GreeterServant extends GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        final String name = request.getName();
        final HelloReply reply = HelloReply.newBuilder().setMessage("Hello, " + name).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
