package TP2.ej3;

public class StackThreadSafe {
    private static final int MAX_SIZE = 10;
    private int top = 0;
    private final int[] values = new int[MAX_SIZE];

    public synchronized void push(final int n) {
        if (top == MAX_SIZE) {
            throw new IllegalStateException("stack full");
        }
        values[top++] = n;
    }

    public synchronized int pop() {
        if (top == 0) {
            throw new IllegalStateException("stack empty");
        }
        return values[--top];
    }
    /**
     * Usar synchronized de metodo es equivalente a envolver el metodo en synchronized(this). Todos los threads que
     * usen la misma instancia del stack competiran POR EL MISMO LOCK(monitor) tanto al llamar a push() como a pop().
     */
}
