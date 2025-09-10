package ar.edu.itba.pod.concurrency.iii.pubsub;

import java.util.concurrent.BlockingQueue;

public class NumbersConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private final int poisonPill;
    private static int sum = 0;

    public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    public void run() {
        try {
            while (true) {
                Integer number = queue.take();
                sum += number;
                if (number.equals(poisonPill)) {
                    // trabajo HASTA que me toque poisonPill
                    System.out.println("FIN THREAD " + Thread.currentThread().getName() + " RESULTADO: " + sum);
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " result: " + number);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}