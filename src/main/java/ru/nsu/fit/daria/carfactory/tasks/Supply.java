package ru.nsu.fit.daria.carfactory.tasks;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Product;
import ru.nsu.fit.daria.carfactory.threadpool.Task;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Supply<T extends Product> implements Task {
    private final CarFactory factory;
    private final Storage<T> storage;
    private final int price;
    private int delay;
    private final Class<T> itemClass;
    private static final Logger logger = Logger.getLogger(Supply.class.getName());

    public Supply(CarFactory factory, Storage<T> storage, int delay, int price, Class<T> itemClass){
        this.factory = factory;
        this.storage = storage;
        this.delay = delay;
        this.price = price;
        this.itemClass = itemClass;
    }

    @Override
    public String getTaskName() {
        return "Supply" + itemClass.getName();
    }

    @Override
    public void performWork(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                System.out.println("sleepin for " + delay);
                Thread.sleep(delay);
                System.out.println("slept");
                long itemID = factory.generateID();
                T item = itemClass .getDeclaredConstructor(long.class).newInstance(itemID);
                storage.put(item);
                factory.closeItemOrder(price);
            } catch (InterruptedException e) {
                logger.info(Thread.currentThread().getName() + " :: INTERRUPTED");
                break;
            }
            catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void changeParams(int newParam) {
        System.out.println("settin new param" + newParam);
        this.delay = newParam;
    }

}
