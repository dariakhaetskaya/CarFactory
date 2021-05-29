package ru.nsu.fit.daria.carfactory;

import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import ru.nsu.fit.daria.carfactory.tasks.BuildCar;
import ru.nsu.fit.daria.carfactory.tasks.SellCar;
import ru.nsu.fit.daria.carfactory.tasks.Supply;
import ru.nsu.fit.daria.carfactory.threadpool.Task;
import ru.nsu.fit.daria.carfactory.threadpool.ThreadPool;
import ru.nsu.fit.daria.carfactory.util.IdGenerator;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.logging.Logger;

public class CarFactory {
    private static final Logger logger = Logger.getLogger(CarFactory.class.getName());
    private final IdGenerator idGenerator = new IdGenerator();
    private Properties properties;

    private final Storage<Engine> engineStorage;
    private final Storage<CarBody> carBodyStorage;
    private final Storage<Wheel> wheelStorage;
    private final Storage<Car> carStorage;
    private final AtomicInteger producedCarCount;

    private int factoryBudget;

    private final ThreadPool workerThreadPool;
    private final ThreadPool supplierThreadPool;
    private final ThreadPool dealerThreadPool;

    Task supplyWheels;
    Task supplyEngine;
    Task supplyCarBody;
    Task sellingOrder;
    Task buildingOrder;

    public CarFactory(){
        logger.info("CAR FACTORY :: STARTING");
        try {
            properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("config.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        carBodyStorage = new Storage<>(Integer.parseInt(properties.getProperty("CarBodyStorageCapacity")), "CarBodyStorage");
        engineStorage = new Storage<>(Integer.parseInt(properties.getProperty("EngineStorageCapacity")), "EngineStorage");
        wheelStorage = new Storage<>(Integer.parseInt(properties.getProperty("WheelStorageCapacity")), "WheelStorage");
        carStorage = new Storage<>(Integer.parseInt(properties.getProperty("CarStorageCapacity")), "CarStorage");
        factoryBudget = Integer.parseInt(properties.getProperty("InitialBudget"));
        int supplierDelay = Integer.parseInt(properties.getProperty("SupplierDelay"));
        int dealerDelay = Integer.parseInt(properties.getProperty("DealerDelay"));
        int carPrice = Integer.parseInt(properties.getProperty("CarPrice"));
        int sparePartPrice = Integer.parseInt(properties.getProperty("SparePartPrice"));
        int dealerCount = Integer.parseInt(properties.getProperty("NumberOfDealers"));
        int workerCount = Integer.parseInt(properties.getProperty("NumberOfWorkers"));
        int supplierCount = Integer.parseInt(properties.getProperty("NumberOfSuppliers"));
        producedCarCount = new AtomicInteger(0);

        // create thread pools
        supplierThreadPool = new ThreadPool(supplierCount * 3);
        workerThreadPool = new ThreadPool(workerCount);
        dealerThreadPool = new ThreadPool(dealerCount);

        // create suppliers
        supplyWheels = new Supply<>(this, wheelStorage, supplierDelay, sparePartPrice, Wheel.class);
        supplyEngine = new Supply<>(this, engineStorage, supplierDelay, sparePartPrice, Engine.class);
        supplyCarBody = new Supply<>(this, carBodyStorage, supplierDelay, sparePartPrice, CarBody.class);

        // create workers
        buildingOrder = new BuildCar(this);

        // create dealers
        sellingOrder = (new SellCar(this, carPrice, dealerDelay));

        // start production
        Thread routine = new Thread(() -> {
            while (carStorage.getItemCount() < carStorage.getStorageCapacity()){
                supplierThreadPool.addTask(supplyWheels);
                supplierThreadPool.addTask(supplyEngine);
                supplierThreadPool.addTask(supplyCarBody);
                workerThreadPool.addTask(buildingOrder);
                dealerThreadPool.addTask(sellingOrder);
            }
        });

        routine.start();

    }

    public void stopFactory(){
        logger.info("FACTORY :: STOPPING WORKING");
        logger.info("TOTAL BALANCE: " + factoryBudget);

        workerThreadPool.shutdown();
        supplierThreadPool.shutdown();
        dealerThreadPool.shutdown();
        logger.info("FACTORY :: FINISHED WORKING");
    }

    public int getFactoryBudget() {
        return factoryBudget;
    }

    public int getProducedCarCount() {
        return producedCarCount.get();
    }

    public int getEngineStorageSize(){
        return engineStorage.getItemCount();
    }

    public int getWheelStorageSize(){
        return wheelStorage.getItemCount();
    }

    public int getCarBodyStorageSize(){
        return carBodyStorage.getItemCount();
    }

    public int getCarStorageSize(){
        return carStorage.getItemCount();
    }

    public long generateID(){
        return idGenerator.get();
    }

    public void closeCarOrder(){
        producedCarCount.getAndIncrement();
        int salary = Integer.parseInt(properties.getProperty("WorkerSalary"));
        factoryBudget -= salary;
    }

    public void closeItemOrder(int price){
        factoryBudget -= price;
    }

    public void closeCarSellContract(int price){
        factoryBudget += price;
    }

    public void setDealerDelay(int dealerDelay) {
       sellingOrder.changeParams(dealerDelay);
    }

    public void setWheelSupplierDelay(int supplierDelay) {
        supplyWheels.changeParams(supplierDelay);
    }

    public void setEngineSupplierDelay(int supplierDelay) {
        supplyEngine.changeParams(supplierDelay);
    }

    public void setCarBodySupplierDelay(int supplierDelay) {
        supplyCarBody.changeParams(supplierDelay);
    }

    public Storage<Engine> passEngineStorageKey(){
        return engineStorage;
    }
    public Storage<CarBody> passCarBodyStorageKey(){
        return carBodyStorage;
    }
    public Storage<Wheel> passWheelStorageKey(){
        return wheelStorage;
    }
    public Storage<Car> passCarStorageKey(){
        return carStorage;
    }



}
