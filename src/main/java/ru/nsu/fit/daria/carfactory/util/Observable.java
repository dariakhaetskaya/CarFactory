package ru.nsu.fit.daria.carfactory.util;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void update();
//    void updateCarCount();
//    void updateCarsInStorage();
//    void updateWheelsInStorage();
//    void updateEnginesInStorage();
//    void updateCarBodiesInStorage();
}
