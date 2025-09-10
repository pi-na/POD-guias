package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.GreeterGrpc;
import ar.edu.itba.pod.grpc.HelloReply;
import ar.edu.itba.pod.grpc.HelloRequest;
import io.grpc.ManagedChannel;

/*
* ATENCION! Primero es necesario `mvn clean install`, esto genera los stub y clases grpc.
* Luego es necesario importar los GRPC stub, request y reply para poder usarlos.
* */

public class GreeterClient {
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public GreeterClient(ManagedChannel channel){
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public String callHello(final String name){
        final var helloRequest = HelloRequest.newBuilder().setName(name).build();
        final HelloReply reply = blockingStub.sayHello(helloRequest);
        return reply.getMessage();
    }
}
