package ru.nsu.fit.daria.carfactory.threadpool;

import java.util.ArrayDeque;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class PooledThread extends Thread {
    AtomicBoolean shutdownRequired = new AtomicBoolean(false);
    private final ArrayDeque<ThreadPoolTask> taskQueue;
    private static final Logger logger = Logger.getLogger(PooledThread.class.getName());

    public PooledThread(String name, ArrayDeque<ThreadPoolTask> taskQueue){
        super(name);
        this.taskQueue = taskQueue;
    }

    public void interruptPooledThread(){
        this.interrupt();
        shutdownRequired.set(true);
    }

    private void performTask(ThreadPoolTask t){
        t.prepare();
        try {
            t.go();
        } catch (InterruptedException e){
            t.interrupted();
            shutdownRequired.set(true);
            return;
        }
        t.finish();
    }

    public void run(){
        ThreadPoolTask toExecute;
        while (!shutdownRequired.get()){
            synchronized (taskQueue){
                if(taskQueue.isEmpty()){
                    try {
                        taskQueue.wait(10);
                    } catch (InterruptedException e){
                        logger.info("POOLED THREAD:: THREAD WAS INTERRUPTED: " + getName());
                        break;
                    }
                    continue;
                } else {
                    toExecute = taskQueue.remove();
                }
            }
            logger.info(getName() + " :: GOT THE JOB: " + toExecute.getTaskName());
            performTask(toExecute);
        }
    }
}
