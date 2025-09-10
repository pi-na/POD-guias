```java
package ar.edu.itba.pod.concurrency.threads.tp.e1;
// imports
public class ConcurrentThreads {
   public static class T1 implements Runnable {
       @Override
public void run() { System.out.print("A"); System.out.print("B");
} }
   public static class T2 implements Runnable {
       @Override
public void run() { System.out.print("1"); System.out.print("2");
} }
public static void main(final String[] args) {
final ExecutorService pool = Executors.newFixedThreadPool(2);
       try {
           pool.execute(new T1());
           pool.execute(new T2());
           pool.shutdown();
if (!pool.awaitTermination(800, TimeUnit.MILLISECONDS)) { pool.shutdownNow();
           }
       } catch (InterruptedException e) {
           pool.shutdownNow();
       }
} }
```

_Listar todas las posibles salidas que podrían obtenerse cuando se ejecuta el siguiente código en un sistema multi-core_

```
A
B
1
2
```

```
A
1
B
2
```

```
A
1
2
B
```

```
1
2
A
B
```

```
1
A
2
B
```

```
1
A
B
2
```