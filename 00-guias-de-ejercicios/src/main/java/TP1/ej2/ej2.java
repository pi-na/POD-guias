package TP1.ej2;

// imports
public class ej2 {
    // ThreadStateViewer
    public static void main(String[] args) throws InterruptedException {
        String lock = "lock";           // Todos los objetos tienen un flag interno de *lock*
        Thread thread = new Thread(() -> {
            System.out.printf("Hello!, my state is %s%n", Thread.currentThread().getState());       // Hello!, my state is NEW
            try {
                Thread.sleep(2000);
                synchronized (lock) {   // Solo entras al bloque si adquiris el lock
                    lock.wait();        // Libero el lock, me quedo "waiting" hasta que algun otro thread haga un notify / notifyAll
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.printf("Thread state: %s%n", thread.getState());     // Thread state: RUNNABLE
        thread.start();
        Thread.sleep(500);
        System.out.printf("Thread state: %s%n", thread.getState());     // Thread state: TIMED_WAITING
        Thread.sleep(2000);
        System.out.printf("Thread state: %s%n", thread.getState());     // Thread state: WAITING
        synchronized (lock) {           // Se liberara el lock cuando el otro thread haga wait
            lock.notifyAll();           // Levanta a los threads que esten waiting, y estos intentan conseguir el lock
        }
        thread.join();
        System.out.printf("Thread state: %s%n", thread.getState());     // Thread state: TERMINATED
    }
}
