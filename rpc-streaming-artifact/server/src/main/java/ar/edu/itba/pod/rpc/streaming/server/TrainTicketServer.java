package ar.edu.itba.pod.rpc.streaming.server;

import ar.edu.itba.pod.grpc.trainTickets.Train;
import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class TrainTicketServer extends ar.edu.itba.pod.grpc.trainTickets.TrainTicketServiceGrpc.TrainTicketServiceImplBase {
    TicketRepository ticketRepository = new TicketRepository();

    @Override
    public void getTrainsForDestination(StringValue request, StreamObserver<Train> responseObserver) {
        String destination = request.getValue();

        List<ar.edu.itba.pod.grpc.server.ticket.repository.Train> toReturn = ticketRepository.getAvailability(destination);

        toReturn.forEach(train -> responseObserver.onNext(Train.newBuilder()
                .setId(train.id())
                .setDestination(train.destination())
                .setAvailableCount(train.availableSeats())
                .setTime(train.time())
                .build()
        ));
        responseObserver.onCompleted();
    }
}
