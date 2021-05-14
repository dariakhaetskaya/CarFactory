package ru.nsu.fit.daria.carfactory;

import ru.nsu.fit.daria.carfactory.products.Car;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import ru.nsu.fit.daria.carfactory.staff.Dealer;
import ru.nsu.fit.daria.carfactory.staff.Supplier;
import ru.nsu.fit.daria.carfactory.util.IdGenerator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


public class CarFactory {
    private static final Logger logger = Logger.getLogger(CarFactory.class.getName());
    private final IdGenerator idGenerator = new IdGenerator();
    private Properties properties;
    private LinkedList<Thread> threads;

    private final Storage<Engine> engineStorage;
    private final Storage<CarBody> carBodyStorage;
    private final Storage<Wheel> wheelStorage;
    private final Storage<Car> carStorage;

    private final AtomicLong producedCarCount;
    private int factoryBuget;
    private int supplierDelay;
    private int dealerDelay;
    private int carPrice;
    private int dealerCount;

    public CarFactory(){
        logger.info("CAR FACTORY :: STARTING");
        try {
            properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("config.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        threads = new LinkedList<>();
        carBodyStorage = new Storage<CarBody>(Integer.parseInt(properties.getProperty("CarBodyStorageCapacity")), "CarBodyStorage");
        engineStorage = new Storage<Engine>(Integer.parseInt(properties.getProperty("EngineStorageCapacity")), "EngineStorage");
        wheelStorage = new Storage<Wheel>(Integer.parseInt(properties.getProperty("WheelStorageCapacity")), "WheelStorage");
        carStorage = new Storage<Car>(Integer.parseInt(properties.getProperty("CarStorageCapacity")), "CarStorage");
        factoryBuget = Integer.parseInt(properties.getProperty("InitialBudget"));
        supplierDelay = Integer.parseInt(properties.getProperty("SupplierDelay"));
        dealerDelay = Integer.parseInt(properties.getProperty("DealerDelay"));
        dealerCount = Integer.parseInt(properties.getProperty("NumberOfDealers"));
        producedCarCount = new AtomicLong(0);

        // create controller

        threads.add(new Thread(new Controller(this,
                Integer.parseInt(properties.getProperty("NumberOfWorkers")),
                Integer.parseInt(properties.getProperty("WorkerSalary")))));

        threads.getLast().start();

        // create suppliers

        threads.add(new Thread(new Supplier<Engine>(this, engineStorage, supplierDelay, carPrice, Engine.class)));
        threads.getLast().start();

        threads.add(new Thread(new Supplier<Wheel>(this, wheelStorage, supplierDelay, carPrice, Wheel.class)));
        threads.getLast().start();

        threads.add(new Thread(new Supplier<CarBody>(this, carBodyStorage, supplierDelay, carPrice, CarBody.class)));
        threads.getLast().start();

        // create dealers

        for (int i = 0; i < dealerCount; i++){
            threads.add(new Thread(new Dealer(this, dealerDelay, carPrice)));
            threads.getLast().start();
        }

    }

    public Long generateID(){
        return idGenerator.get();
    }

    public int closeCarOrder(){
        producedCarCount.getAndIncrement();
        int salary = Integer.parseInt(properties.getProperty("WorkerSalary"));
//        factoryBuget.addAndGet((-1)*salary);
        factoryBuget -= salary;
        producedCarCount.incrementAndGet();
        return salary;
    }

    public void closeItemOrder(int price){
        factoryBuget -= price;
    }

    public void closeCarSellContract(int price){
        factoryBuget += price;
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



    public void stopFactory(){
        logger.info("FACTORY :: STOPPING WORKING");
        logger.info("TOTAL BALANCE: " + factoryBuget);
        try {
            for (Thread thread: threads){
                thread.interrupt();
                thread.join();
            }
            logger.info("FACTORY :: FINISHED WORKING");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
