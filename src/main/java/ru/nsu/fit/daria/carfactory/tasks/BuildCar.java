package ru.nsu.fit.daria.carfactory.tasks;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.products.Car.CarBuilder;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import ru.nsu.fit.daria.carfactory.threadpool.Task;

import static ru.nsu.fit.daria.carfactory.util.Utils.ensuringNotNull;

public class BuildCar implements Task {
    private final CarFactory carFactory;
    private final long workerID; // for logging

    private final Storage<Engine> engineStorage;
    private final Storage<CarBody> carBodyStorage;
    private final Storage<Wheel> wheelStorage;
    private final Storage<Car> carStorage;

    public BuildCar(CarFactory carFactory){
        this.carFactory = carFactory;
        workerID = carFactory.generateID();
        engineStorage = carFactory.passEngineStorageKey();
        carBodyStorage = carFactory.passCarBodyStorageKey();
        wheelStorage = carFactory.passWheelStorageKey();
        carStorage = carFactory.passCarStorageKey();
    }

    @Override
    public String getName() {
        return "Build car. Worker ID: " + workerID;
    }

    @Override
    public void performWork() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            CarBuilder currentCar = new CarBuilder(carFactory.generateID());
            currentCar.installWheel(ensuringNotNull(wheelStorage.get()));
            currentCar.installEngine(engineStorage.get());
            currentCar.installBody(carBodyStorage.get());
            carStorage.put(currentCar.finishBuild());
            carFactory.closeCarOrder();
        }
    }
}
