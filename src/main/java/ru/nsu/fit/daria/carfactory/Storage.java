package ru.nsu.fit.daria.carfactory;

import ru.nsu.fit.daria.carfactory.products.Product;
import ru.nsu.fit.daria.carfactory.util.Observable;
import ru.nsu.fit.daria.carfactory.util.Observer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Storage<T extends Product> {
    private ArrayDeque<T> items;
    private final int storageCapacity;
    private final String storageName;
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final Object monitor = new Object();
    private final ArrayList<Observer> observers;

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public int getItemCount(){
        return items.size();
    }

    public Storage(int capacity, String name){
        storageName = name;
        storageCapacity = capacity;
        items = new ArrayDeque<>();
        logger.info(storageName + " :: CREATED");
        observers = new ArrayList<>();
    }

    public void put (T newItem) throws InterruptedException {
        synchronized (monitor) {
            if (items.size() >= storageCapacity) {
                try {
                    logger.info(storageName + " :: STORAGE IS FULL");
                    monitor.wait();
                } catch (InterruptedException e) {
                    logger.info(storageName + " :: INTERRUPTED IN WAIT");
                    throw e;
                }
            }
            logger.info(storageName + " :: GOT NEW ITEM :: " + newItem.toString());
            items.add(newItem);
//            update();
            monitor.notifyAll();
            logger.info(storageName + " :: NOTIFIED");
        }
    }

    public T get () throws InterruptedException {
        synchronized (monitor) {
            while (true) {
                try {
                    logger.info(storageName + " SIZE " + items.size());
                    if (!items.isEmpty()) {
                        T item = items.getLast();
                        items.pop();
                        monitor.notifyAll();
//                        update();
                        logger.info(storageName + " :: PASSING PRODUCT");
                        return item;
                    } else {
                        logger.info(storageName + " :: WAITING FOR A SPARE");
                        monitor.wait();
                        logger.info(storageName + " :: WOKE UP");
                    }
                } catch (InterruptedException e) {
                        logger.info(storageName + " :: INTERRUPTED IN WAIT");
                        throw e;
                    }
            }
        }
    }

}

