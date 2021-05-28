package ru.nsu.fit.daria.carfactory.util;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void update();
}
