package ru.nsu.fit.daria.carfactory.tasks;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.ManagedThread;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.threadpool.Task;

public class SellCar implements Task {
    private final long dealerID;
    private final CarFactory factory;
    private final Storage<Car> carStorage;
    private final int carPrice;
    private int delay;

    public SellCar(CarFactory factory, int carPrice, int delay){
        dealerID = factory.generateID();
        this.factory = factory;
        this.carPrice = carPrice;
        this.delay = delay;
        carStorage = factory.passCarStorageKey();
    }

    @Override
    public String getTaskName() {
        return "Sell car. Dealer ID: " + dealerID;
    }

    @Override
    public void performWork() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(delay);
                carStorage.get();
                factory.closeCarSellContract(carPrice);
            } catch (InterruptedException e) {
                throw e;
            }
        }
    }

    @Override
    public void changeParams(int newParam) {
        this.delay = newParam;
    }

}
