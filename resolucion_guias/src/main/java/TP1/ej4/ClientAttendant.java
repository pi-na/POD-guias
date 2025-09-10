//package TP1.ej4;
//
//import java.util.concurrent.Callable;
//
//public class ClientAttendant implements Callable<Integer > {
//
//    private final BranchClientQueueService clientService;
//    private final ClientPriority priority;
//    public ClientAttendant(BranchClientQueueService clientService,
//                           ClientPriority priority) {
//        this.clientService = clientService;
//        this.priority = priority;
//    }
//    @Override
//    public Integer call() throws Exception {
//        boolean stillWorking = true;
//        while (stillWorking) { //if 3 cycles with no client end.
//            //get one client and sleep for random amount of seconds to
//            simulate service time
//            // or if no client sleep to simulate waiting time.
//        }
//        return 0; // how many clients
//    }
//}