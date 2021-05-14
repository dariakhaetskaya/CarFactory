package ru.nsu.fit.daria.carfactory.spares;

import ru.nsu.fit.daria.carfactory.products.Product;

public abstract class CarPart extends Product {
    protected CarPart (Long id){
        super(id);
    }
}
