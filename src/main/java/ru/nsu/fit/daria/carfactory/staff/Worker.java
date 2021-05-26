package ru.nsu.fit.daria.carfactory.staff;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.products.Car.CarBuilder;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;

import static ru.nsu.fit.daria.carfactory.util.Utils.ensuringNotNull;

public class Worker implements Runnable{
    private final CarFactory carFactory;
    private final long workerID; // for logging
    private int workersMoney;

    private final Storage<Engine> engineStorage;
    private final Storage<CarBody> carBodyStorage;
    private final Storage<Wheel> wheelStorage;
    private final Storage<Car> carStorage;

    public Worker(CarFactory carFactory){
        this.carFactory = carFactory;
        workerID = carFactory.generateID();
        engineStorage = carFactory.passEngineStorageKey();
        carBodyStorage = carFactory.passCarBodyStorageKey();
        wheelStorage = carFactory.passWheelStorageKey();
        carStorage = carFactory.passCarStorageKey();
    }

    @Override
    public void run(){
        try {
            CarBuilder currentCar = new CarBuilder(carFactory.generateID());
            currentCar.installBody(carBodyStorage.get());
            currentCar.installEngine(engineStorage.get());
            //for (int i = 0; i < 4; i++){
                currentCar.installWheel(ensuringNotNull(wheelStorage.get()));
            //}
            carStorage.put(currentCar.finishBuild());
            workersMoney += carFactory.closeCarOrder();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
