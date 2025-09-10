//package TP1.ej4;
//
//
//import java.util.concurrent.Callable;
//
//// imports
//public class Receptionist implements Callable<Integer > {
//    private static final Integer AMOUNT_OF_CLIENTS = 100; private final IBranchClientQueueService clientService;
//    public Receptionist(final IBranchClientQueueService clientService) {
//        this.clientService = clientService;
//    }
//    @Override
//    public Integer call() throws Exception {
//        for (int i = 0; i < AMOUNT_OF_CLIENTS; i++) {
//            // simulate one client and enqueue
//            // sleep for a couple of random seconds.
//        }
//        return AMOUNT_OF_CLIENTS; }
//
//}