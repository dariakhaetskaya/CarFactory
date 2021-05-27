package ru.nsu.fit.daria.carfactory.threadpool;

import java.util.ArrayDeque;
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
        while (true){
            synchronized (taskQueue){
                if(taskQueue.isEmpty()){
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e){
                        logger.info("POOLED THREAD:: THREAD WAS INTERRUPTED: " + getName());
                        break;
                    }
                    continue;
                } else {
                    toExecute = taskQueue.remove();
                }
            }
            logger.info(getName() + " :: GOT THE JOB: " + toExecute.getName());
            performTask(toExecute);
            if (shutdownRequired.get()){
                break;
            }
        }
    }
}
