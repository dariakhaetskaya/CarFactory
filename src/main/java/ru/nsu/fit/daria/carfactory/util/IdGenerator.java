package ru.nsu.fit.daria.carfactory.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private final AtomicLong atomicLong;

    public IdGenerator(){
        atomicLong = new AtomicLong(1);
    }

    public Long get(){
        return atomicLong.getAndIncrement();
    }
}
