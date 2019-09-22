package com.qwwuyu.server.utils;

import java.util.LinkedList;

public class LimitQueue<E> {
    private int limit;
    private LinkedList<E> queue = new LinkedList<E>();

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    public void offer(E e) {
        if (queue.size() >= limit) {
            queue.poll();
        }
        queue.offer(e);
    }

    public E get(int position) {
        if (0 <= position && position < queue.size()) return queue.get(position);
        return null;
    }

    public E getLast() {
        if (queue.isEmpty()) return null;
        return queue.getLast();
    }

    public E getFirst() {
        if (queue.isEmpty()) return null;
        return queue.getFirst();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return queue.size();
    }
}
