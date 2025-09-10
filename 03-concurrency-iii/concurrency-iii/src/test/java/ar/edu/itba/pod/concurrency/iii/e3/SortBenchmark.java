package ar.edu.itba.pod.concurrency.iii.e3;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.time.LocalTime;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Benchmar to compare between {@link Arrays#parallelSort(int[])} and
 * {@link Arrays#sort(int[])}
 */
public class SortBenchmark {
    private final int count_array_10m = 10000000;
    private final int count_array_25m = 25000000;
    private final int count_array_50m = 50000000;
    int[] array_10m = new int[count_array_10m];
    int[] array_25m = new int[count_array_25m];
    int[] array_50m = new int[count_array_50m];

    private void init_array_random(int[] array, int count_array){
        Random random = new Random();
        for(int i = 0; i<count_array; i++){
            array[i] = random.nextInt();
        }
    }

    private double benchmark_parallel(int[] array, int iterations){
        ArrayList<Long> results = new ArrayList<>();
        for(int i=0; i<iterations;i++){
            int[] benchmark_array = Arrays.copyOf(array, array.length); // 👈 importante
            Instant start_time = Instant.now();
            Arrays.parallelSort(benchmark_array);
            results.add(Duration.between(start_time, Instant.now()).toMillis());
        }
        double avgMs = results.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
        return avgMs;
    }

    private double benchmark_sort(int[] array, int iterations){
        ArrayList<Long> results = new ArrayList<>();
        for(int i=0; i<iterations;i++){
            int[] benchmark_array = Arrays.copyOf(array, array.length); // 👈 importante
            Instant start_time = Instant.now();
            Arrays.sort(benchmark_array);
            results.add(Duration.between(start_time, Instant.now()).toMillis());
        }
        double avgMs = results.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
        return avgMs;
    }
    @Test
    public void benchmark_all() {
        // Inicializar arrays con randoms
        init_array_random(array_10m, count_array_10m);
        init_array_random(array_25m, count_array_25m);
        init_array_random(array_50m, count_array_50m);

        int iterations = 4;

        System.out.println("== Benchmark con " + iterations + " iteraciones ==");

        // Array 10M
        System.out.println("Array 10M - parallelSort: " +
                benchmark_parallel(array_10m, iterations) + " ms");
        System.out.println("Array 10M - sort: " +
                benchmark_sort(array_10m, iterations) + " ms");

        // Array 25M
        System.out.println("Array 25M - parallelSort: " +
                benchmark_parallel(array_25m, iterations) + " ms");
        System.out.println("Array 25M - sort: " +
                benchmark_sort(array_25m, iterations) + " ms");

        // Array 50M
        System.out.println("Array 50M - parallelSort: " +
                benchmark_parallel(array_50m, iterations) + " ms");
        System.out.println("Array 50M - sort: " +
                benchmark_sort(array_50m, iterations) + " ms");
    }

}
