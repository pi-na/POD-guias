package ar.edu.itba.pod.concurrency.exercises.e1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Basic implementation of {@link GenericService}.
 */
public  class GenericServiceImpl implements GenericService {

    private Queue<String> strings = new LinkedList<>();
    private Integer visits = 0;

    public GenericServiceImpl() {
    }

    @Override
    public String echo(String message) {
        return message;
    }

    @Override
    public String toUpper(String message) {
        return message == null ? message : message.toUpperCase();
    }

    @Override
    public void addVisit() {
        visits++;
    }

    @Override
    public int getVisitCount() {
        return visits;
    }

    @Override
    public boolean isServiceQueueEmpty() {
        return strings.isEmpty();
    }

    @Override
    public void addToServiceQueue(String name) {
        strings.add(name);
    }

    @Override
    public String getFirstInServiceQueue() {
        if(strings.isEmpty()) throw new IllegalStateException("No one in queue");
        return strings.poll();
    }
}
