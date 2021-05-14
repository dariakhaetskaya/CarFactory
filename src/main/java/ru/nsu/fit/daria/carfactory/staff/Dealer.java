package ru.nsu.fit.daria.carfactory.staff;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Car;

public class Dealer implements Runnable {
    private CarFactory factory;
    private Storage<Car> carStorage;
    private int carPrice;
    private int delay;

    public Dealer(CarFactory factory, int carPrice, int delay){
        this.factory = factory;
        this.carPrice = carPrice;
        this.delay = delay;
        carStorage = factory.passCarStorageKey();
    }

    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                Car car = carStorage.get();
                factory.closeCarSellContract(carPrice);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
