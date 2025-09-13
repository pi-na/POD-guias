package TP2.ej2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

public class StackTest {

    private Callable<Void> assertStack(Stack stack, List<Integer> elems){
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

    @Test
    public void testRespuestas(){
        int TIMES = 1000;
        Stack stack = new Stack();
        Thread[] pool = new Thread[TIMES];      // Es un pool pero es un array de threads
        /**
         * Cada thread pushea y popea constantemente. No esta protegido el acceso de lectura ni escritura a "top",
         * el indice que maneja el tope del stack. Entonces, puede pasar que varios threads hagan varios push() a la vez
         * o push() a la vez que otro thread hace pop(). Asi, puede pasar que el indice quede apuntando a una posicion
         * fuera del tamaño del arreglo `int values[] = new int[MAX_SIZE]`. Esto sucede por que quizas varios threads
         * se encuentran con que el chequeo de top les da correcto, cuando el indice estaba en un borde del arreglo.
         * Tambien, por diseño del Stack, se podrian lanzar las excepciones de StackEmpty o StackFull.
         */
        for(int i = 0; i < TIMES; i++){
            pool[i] = new Thread(){
                @Override
                public void run(){
                    for(int i = 0; i < TIMES; i++){
                        stack.push(1);
                        stack.pop();
                    }
                }
            };
            pool[i].start();
        }
        /**
         * Al hacer join, el thread que ejecuta el testRespuestas() se detiene para cada thread pool[i] que
         * aun no haya terminado.
         **/
        try{
            for(int i = 0; i < TIMES; i++){
                pool[i].join();
            }
        } catch (InterruptedException e){
            return;
        }
    }
}
