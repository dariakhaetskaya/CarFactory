package ru.nsu.fit.daria.carfactory;

import java.util.Observable;
import java.util.Observer;
import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import ru.nsu.fit.daria.carfactory.staff.Worker;

import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Controller implements Observer, Runnable {
    private CarFactory factory;
    private Storage<Engine> engineStorage;
    private Storage<CarBody> carBodyStorage;
    private Storage<Wheel> wheelStorage;
    private Storage<Car> carStorage;
    int carsStorageCapacity;
    int carsInStorage;
    private int workerSalary;

    private ThreadPoolExecutor threadPool;
    private final Object syncObject = new Object();


    public Controller(CarFactory factory,  int threadCount, int workerSalary){
        this.factory = factory;
        this.carBodyStorage = factory.passCarBodyStorageKey();
        this.engineStorage = factory.passEngineStorageKey();
        this.carStorage = factory.passCarStorageKey();
        this.wheelStorage = factory.passWheelStorageKey();
        this.workerSalary = workerSalary;

        carsStorageCapacity = carStorage.getStorageCapacity();
        carStorage.addObserver(this);
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                int workerCount = carsStorageCapacity - carsInStorage - threadPool.getActiveCount() - threadPool.getQueue().size();
                for (int i = 0; i < workerCount; i++){
                    threadPool.execute(new Worker(factory)); // may de wrong constructor
                }

                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg){
        carsInStorage = (Integer) arg;
        synchronized (this){
            notify();
        }
    }

}
