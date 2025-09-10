package ar.edu.itba.pod.concurrency.exercises.e2;

import ar.edu.itba.pod.concurrency.exercises.e1.GenericService;
import ar.edu.itba.pod.concurrency.exercises.e1.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ThreadTest {
    private int threadCount = 5;
    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    /**
     * Makes VISITS_BY_THREAD visits to the service
     */
    private final Runnable visitor = () -> {
        service.addVisit();
    };

    @Test
   public final void testVisit(){
        Thread[] threads = new Thread[threadCount];
        for(int i = 0; i < threadCount; i++){
            threads[i] = new Thread(visitor);
            threads[i].start();
        }
        System.out.println(service.getVisitCount());
    }

    @Test
    public final void test() {
        assertEquals("No one in queue", "No one in queue");
    }

}
