package ru.nsu.fit.daria.carfactory.products;

public abstract class Product {
    private final Long id;

    public Product(Long ProductID){
        id = ProductID;
    }

    public Long getID(){
        return id;
    }
}
