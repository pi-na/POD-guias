package ar.edu.itba.pod.rpc.streaming.client;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("rpc-streaming-artifact Client Starting ...");
        logger.info("grpc-com-patterns Client Starting ...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            ar.edu.itba.pod.grpc.trainTickets.TrainTicketServiceGrpc.TrainTicketServiceBlockingStub blockingStub =
                    ar.edu.itba.pod.grpc.trainTickets.TrainTicketServiceGrpc.newBlockingStub(channel);
            StringValue request = StringValue.of("Mar del Plata");
            Iterator<ar.edu.itba.pod.grpc.trainTickets.Train> trainsForDestination =
                    blockingStub.getTrainsForDestination(request);
            while (trainsForDestination.hasNext()) {
                System.out.println(trainsForDestination.next());
            }

            } finally {
            channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}
