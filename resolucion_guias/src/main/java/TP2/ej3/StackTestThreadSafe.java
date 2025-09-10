package TP2.ej3;

import TP2.ej2.Stack;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**

java.lang.RuntimeException: java.lang.AssertionError:
Expected :1
Actual   :5
<Click to see difference>

**/

public class StackTestThreadSafe {

    private Callable<Void> assertStack(StackThreadSafe stack, List<Integer> elems){
        return( () -> {
            for(Integer elem : elems){
                stack.push(elem);
                Thread.sleep(100);
            }
            for (int i = elems.size() - 1; i >= 0; i--) {
                assertEquals((int) elems.get(i), stack.pop());
                Thread.sleep(100);
            }
            return null;
        });
    }

    /*
    Este test puede seguir fallando aunque la clase sea thread-safe,
    porque el contrato de un stack compartido no asegura que cada hilo recupere sus propios elementos.
     */
    @Test
    public void testStackThreadSafety(){
        StackThreadSafe stack = new StackThreadSafe();
        ExecutorService pool = Executors.newCachedThreadPool();

        List<Callable<Void>> tasks = List.of(
                assertStack(stack, Arrays.asList(1, 2, 3)),
                assertStack(stack, Arrays.asList(4, 5, 6)),
                assertStack(stack, Arrays.asList(6, 7, 8))
        );
        List<Future<Void>> futures;
        try {
            futures = pool.invokeAll(tasks);
        } catch(Exception e) {
            return;
        }

        for (Future<Void> f : futures) {
            try {
                f.get(); // <-- si hubo AssertionError dentro de algún thread, explota acá
            } catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Test
    public void testRespuestas(){
        int TIMES = 1000;
        StackThreadSafe stack = new StackThreadSafe();
        Thread[] pool = new Thread[TIMES];

        for(int i = 0; i < TIMES; i++){
            pool[i] = new Thread(){
                @Override
                public void run(){
                    for(int i = 0; i < TIMES; i++){
                        while(true){
                            try {
                                stack.push(1);
                            } catch(IllegalStateException e){
                                try {
                                    sleep(50);
                                } catch (InterruptedException ex) {
                                    continue;
                                }
                                continue;
                            }
                            break;
                        }
                        while(true){
                            try {
                                stack.pop();
                            } catch(IllegalStateException e){
                                try {
                                    sleep(50);
                                } catch (InterruptedException ex) {
                                    continue;
                                }
                                continue;
                            }
                            break;
                        }
                    }
                }
            };
            pool[i].start();
        }

        try{
            for(int i = 0; i < TIMES; i++){
                pool[i].join();
            }
        } catch (InterruptedException e){
            return;
        }
    }
}
