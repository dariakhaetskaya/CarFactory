package ru.nsu.fit.daria.carfactory.threadpool;

public interface Task {
    String getTaskName();
    void performWork() throws InterruptedException;
    void changeParams(int newParam);
}
