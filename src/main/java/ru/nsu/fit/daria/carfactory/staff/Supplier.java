package ru.nsu.fit.daria.carfactory.staff;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.Storage;
import ru.nsu.fit.daria.carfactory.products.Product;
import java.lang.reflect.InvocationTargetException;

public class Supplier <T extends Product> implements Runnable{
    private CarFactory factory;
    private Storage<T> storage;
    private final int price;
    private final int delay;
    private Class<T> itemClass;

    public Supplier (CarFactory factory, Storage<T> storage, int delay, int price, Class<T> itemClass){
        this.factory = factory;
        this.storage = storage;
        this.delay = delay;
        this.price = price;
        this.itemClass = itemClass;
    }

    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                Long itemID = factory.generateID();
                T item = (T)itemClass.getDeclaredConstructor(Long.class).newInstance(itemID);
                storage.put(item);
                factory.closeItemOrder(price);
                Thread.sleep(delay);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

}
