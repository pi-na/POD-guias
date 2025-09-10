# Ejercicio 1
Decidir V o F y justificar la respuesta
Son equivalentes estas declaraciones de metodos?

```java
public class A {
    static int method() {
        synchronized (A.class) {
            // "acá va el código que corresponda, con el return correspondiente"
        }
    }
}
```

```java
public class A {
    static synchronized int method() {
        // "acá va el código que corresponda, con el return correspondiente"
    }
}
```

De [https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html]:

_When a thread invokes a synchronized method, it automatically acquires the intrinsic lock for that method's object and releases it when the method returns. The lock release occurs even if the return was caused by an uncaught exception._

_You might wonder what happens when a static synchronized method is invoked, since a static method is associated with a class, not an object. In this case, the thread acquires the intrinsic lock for the Class object associated with the class. Thus access to class's static fields is controlled by a lock that's distinct from the lock for any instance of the class._

Son iguales!

