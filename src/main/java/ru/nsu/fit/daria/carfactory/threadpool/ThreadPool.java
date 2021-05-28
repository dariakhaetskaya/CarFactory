package ru.nsu.fit.daria.carfactory.threadpool;

import java.util.*;
import java.util.logging.Logger;

public class ThreadPool implements TaskListener {
    private final ArrayDeque<ThreadPoolTask> taskQueue = new ArrayDeque<>();
    private final Set<PooledThread> availableThreads = new HashSet<>();

    private static final Logger logger = Logger.getLogger(ThreadPool.class.getName());

    public ThreadPool(int threadCount){
        for (int i = 0; i < threadCount; i++){
            availableThreads.add(new PooledThread("Performer_" + i, taskQueue));
        }
        for (PooledThread pt : availableThreads){
            pt.start();
        }
    }

    @Override
    public void taskStarted(Task t) {
        logger.info("THREAD POOL :: STARTED " + t.getTaskName());
    }

    @Override
    public void taskFinished(Task t) {
        logger.info("THREAD POOL :: FINISHED " + t.getTaskName());
    }

    @Override
    public void taskInterrupted(Task t) {
        logger.info("THREAD POOL :: INTERRUPTED " + t.getTaskName());
    }

    public void addTask(Task t){
        addTask(t, this);
    }

    public void addTask(Task t, TaskListener l){
        synchronized (taskQueue){
            logger.fine("THREAD POOL :: ADDING NEW TASK " + t.getTaskName());
            taskQueue.add(new ThreadPoolTask(t, l));
            taskQueue.notifyAll();
        }
    }

    public void shutdown(){
        logger.info("THREAD POOL :: SHUTTING DOWN POOL... INTERRUPTING THREADS");
        for (PooledThread pt: availableThreads){
            pt.interruptPooledThread();
            logger.info("THREAD POOL :: INTERRUPTED " + pt.getName());
        }

        for (PooledThread pt: availableThreads){
            try {
                pt.join();
                logger.info("THREAD POOL :: FINISHED " + pt.getName());
            } catch (InterruptedException e){
                logger.info("THREAD POOL :: INTERRUPTED WHILE JOINING " + pt.getName());
            }
        }
        logger.info("THREAD POOL :: SHUTDOWN COMPLETED");
    }
}
