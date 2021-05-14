package ru.nsu.fit.daria.carfactory.products;

import ru.nsu.fit.daria.carfactory.CarFactory;
import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import static ru.nsu.fit.daria.carfactory.util.Utils.ensuringNotNull;
import static ru.nsu.fit.daria.carfactory.util.Utils.ensuring;

import java.util.Collections;
import java.util.List;

public class Car extends Product{
    private final Engine engine;
    private final CarBody carBody;
    //private final List<Wheel> wheels;
    private final Wheel wheels;

    private Car(Long id, Engine engine, CarBody carBody, Wheel wheels){
        super(id);
        this.engine = ensuringNotNull(engine);
        this.carBody = ensuringNotNull(carBody);
        this.wheels = ensuringNotNull(wheels);
        //this.wheels = Collections.unmodifiableList(ensuring(wheels, w -> w != null && w.size() == 4));
    }

    public static class CarBuilder{
        private final Long id;
        private Engine engine;
        private CarBody carBody;
        //private List<Wheel> wheels;
        private Wheel wheels;

        public CarBuilder(Long id){
            this.id = id;
        }

        public void installEngine(Engine engine){
            this.engine = engine;
        }

        public void installWheel(Wheel wheel){
           // this.wheels.add(ensuringNotNull(wheel));
            this.wheels = wheel;
        }

        public void installBody(CarBody carBody){
            this.carBody = carBody;
        }

        public Car finishBuild(){
            System.out.println("built a car id :" + id);
            return new Car(id, engine, carBody, wheels);
        }

    }
}
