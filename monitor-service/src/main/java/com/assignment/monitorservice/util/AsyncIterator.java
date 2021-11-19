package com.assignment.monitorservice.util;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncIterator<T> implements Iterator<T> {

    private BlockingQueue<T> queue = new ArrayBlockingQueue<T>(100);
    private T sentinel = (T) new Object();
    private T next;

    private AsyncIterator(final Iterator<T> delegate) {
        new Thread() {
            @Override
            public void run() {
                try{
                    while (delegate.hasNext()) {
                        queue.put(delegate.next());
                    }
                    queue.put(sentinel);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public boolean hasNext() {
        try{
            if (next != null) {
                return true;
            }
            next = queue.take(); // blocks if necessary
            if (next == sentinel) {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public T next() {
        T tmp = next;
        next = null;
        return tmp;
    }
}
