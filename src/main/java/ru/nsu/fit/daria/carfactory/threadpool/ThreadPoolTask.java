package ru.nsu.fit.daria.carfactory.threadpool;

public class ThreadPoolTask {
    private final TaskListener listener;
    private final Task task;

    public ThreadPoolTask(Task t, TaskListener l){
        listener = l;
        task = t;
    }

    void prepare(){
        listener.taskStarted(task);
    }

    void finish(){
        listener.taskFinished(task);
    }

    void interrupted(){
        listener.taskInterrupted(task);
    }

    void go() throws InterruptedException{
        task.performWork();
    }

    String getTaskName(){
        return task.getTaskName();
    }
}
