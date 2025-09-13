package TP1.ej3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ej3 {
    // ExecutorAnalyzer
    private static final Logger logger = LoggerFactory.getLogger(ej3.class);
    private static final int THREAD_COUNT = 4;

    private static final Function<Integer, Callable<Void>> runnerFactory = (Integer index) -> () -> {
        logger.info("Starting runner: {}", index);
        Thread.sleep(1500);
        logger.info("Ending runner: {}", index); return null;
    };

    public static void execute(ExecutorService pool) {
        try {
            List<Future<Void>> futures = IntStream.range(0, THREAD_COUNT).mapToObj(index ->
                    pool.submit(runnerFactory.apply(index))).toList();
            for (Future<Void> future : futures) {
                future.get();
            }
            pool.shutdown();
            if (!pool.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException | ExecutionException e) {
            pool.shutdownNow();
        } }

    public static void main(String[] args) {
        logger.info("Cached Thread Pool"); execute(Executors.newCachedThreadPool());
        logger.info("Fixed Thread Pool"); execute(Executors.newFixedThreadPool(2));
        logger.info("Single Thread Executor"); execute(Executors.newSingleThreadExecutor());
        logger.info("Single Thread Executor but rejecting");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),               // SynchronousQueue no tiene capacidad (no almacena: o se entrega al instante a un hilo libre, o falla).
                new ThreadPoolExecutor.AbortPolicy()); execute(executor);
        // La primera entra (el único hilo está libre) → Starting runner: 0.
        //La segunda (y siguientes) no tienen dónde esperar (el único hilo está ocupado y la cola no guarda nada) ⇒ rechazo inmediato según la política AbortPolicy, que lanza RejectedExecutionException.
    } }

/*
        15:15:48.082 [main] INFO TP1.ej3.ej3 -- Cached Thread Pool
        15:15:48.086 [pool-1-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 0
        15:15:48.086 [pool-1-thread-3] INFO TP1.ej3.ej3 -- Starting runner: 2
        15:15:48.086 [pool-1-thread-2] INFO TP1.ej3.ej3 -- Starting runner: 1
        15:15:48.086 [pool-1-thread-4] INFO TP1.ej3.ej3 -- Starting runner: 3
        15:15:49.587 [pool-1-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 0
        15:15:49.587 [pool-1-thread-4] INFO TP1.ej3.ej3 -- Ending runner: 3
        15:15:49.587 [pool-1-thread-2] INFO TP1.ej3.ej3 -- Ending runner: 1
        15:15:49.589 [pool-1-thread-3] INFO TP1.ej3.ej3 -- Ending runner: 2

        15:15:49.590 [main] INFO TP1.ej3.ej3 -- Fixed Thread Pool
        15:15:49.590 [pool-2-thread-2] INFO TP1.ej3.ej3 -- Starting runner: 1
        15:15:49.590 [pool-2-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 0
        15:15:51.096 [pool-2-thread-2] INFO TP1.ej3.ej3 -- Ending runner: 1
        15:15:51.096 [pool-2-thread-2] INFO TP1.ej3.ej3 -- Starting runner: 2
        15:15:51.096 [pool-2-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 0
        15:15:51.096 [pool-2-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 3
        15:15:52.599 [pool-2-thread-2] INFO TP1.ej3.ej3 -- Ending runner: 2
        15:15:52.599 [pool-2-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 3

        15:15:52.600 [main] INFO TP1.ej3.ej3 -- Single Thread Executor
        15:15:52.601 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 0
        15:15:54.101 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 0
        15:15:54.102 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 1
        15:15:55.605 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 1
        15:15:55.605 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 2
        15:15:57.110 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 2
        15:15:57.111 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 3
        15:15:58.613 [pool-3-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 3

        15:15:58.615 [main] INFO TP1.ej3.ej3 -- Single Thread Executor but rejecting
        15:15:58.616 [pool-4-thread-1] INFO TP1.ej3.ej3 -- Starting runner: 0
        Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@27abe2cd[Not completed, task = TP1.ej3.ej3$$Lambda$24/0x000000080101cca0@3f102e87] rejected from java.util.concurrent.ThreadPoolExecutor@64cee07[Running, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
        at java.base/java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2081)
        at java.base/java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:841)
        at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1376)
        at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:145)
        at TP1.ej3.ej3.lambda$execute$2(ej3.java:24)
        at java.base/java.util.stream.IntPipeline$1$1.accept(IntPipeline.java:180)
        at java.base/java.util.stream.Streams$RangeIntSpliterator.forEachRemaining(Streams.java:104)
        at java.base/java.util.Spliterator$OfInt.forEachRemaining(Spliterator.java:711)
        at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:575)
        at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(AbstractPipeline.java:260)
        at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:616)
        at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:622)
        at java.base/java.util.stream.ReferencePipeline.toList(ReferencePipeline.java:627)
        at TP1.ej3.ej3.execute(ej3.java:24)
        at TP1.ej3.ej3.main(ej3.java:42)
        15:16:00.120 [pool-4-thread-1] INFO TP1.ej3.ej3 -- Ending runner: 0
*/