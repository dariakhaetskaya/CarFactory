package ru.nsu.fit.daria.carfactory.tasks;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.threadpool.Task;

public class SellCar implements Task {
    private final long dealerID;
    private final CarFactory factory;
    private final Storage<Car> carStorage;
    private final int carPrice;
    private final int delay;

    public SellCar(CarFactory factory, int carPrice, int delay){
        dealerID = factory.generateID();
        this.factory = factory;
        this.carPrice = carPrice;
        this.delay = delay;
        carStorage = factory.passCarStorageKey();
    }

    @Override
    public String getName() {
        return "Sell car. Dealer ID: " + dealerID;
    }

    @Override
    public void performWork(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                carStorage.get();
                factory.closeCarSellContract(carPrice);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}
