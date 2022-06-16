package org.texngine;

import java.util.concurrent.atomic.AtomicLong;

public abstract class TeXPriorityTask implements Runnable, Comparable<TeXPriorityTask> {

    private static final AtomicLong orderNumberGenerator = new AtomicLong();

    private final long order = orderNumberGenerator.incrementAndGet();

    private long priority = 0;

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(TeXPriorityTask task) {
        int result = Long.compare(priority, task.priority);

        if (result == 0)
            result = Long.compare(order, task.order);

        return result;
    }

}
