package ru.nsu.fit.daria.carfactory;

import ru.nsu.fit.daria.carfactory.products.Product;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

public class Storage<T extends Product> extends Observable {
    private LinkedList<T> items;
    private final int storageCapacity;
    private final String storageName;

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public int getItemCount(){
        return items.size();
    }

    public Storage(int capacity, String name){
        storageName = name;
        storageCapacity = capacity;
        items = new LinkedList<>();
    }

    public synchronized void put (T newItem) throws InterruptedException {
            if (items.size() == storageCapacity){
                wait();
            }

            items.add(newItem);
            notify();
            setChanged();
            notifyObservers(items.size());
    }

    public synchronized T get () throws InterruptedException {
           while (true){
               if (!items.isEmpty()) {
                   T item = items.getLast();
                   items.pop();
                   notify();
                   setChanged();
                   notifyObservers(items.size());
                   return item;
               } else {
                   wait();
               }
           }
    }

}

