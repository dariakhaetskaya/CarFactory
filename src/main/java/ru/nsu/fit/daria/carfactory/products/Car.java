package ru.nsu.fit.daria.carfactory.products;

import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;

import static ru.nsu.fit.daria.carfactory.util.Utils.*;

public class Car extends Product{
    private final Engine engine;
    private final CarBody carBody;
    private final Wheel wheels;

    private Car(long id, Engine engine, CarBody carBody, Wheel wheels){
        super(id);
        this.engine = ensuringNotNull(engine);
        this.carBody = ensuringNotNull(carBody);
        this.wheels = ensuringNotNull(wheels);
    }

    public static class CarBuilder{
        private final long id;
        private Engine engine;
        private CarBody carBody;
        private Wheel wheels;

        public CarBuilder(long id){
            this.id = id;
        }

        public void installEngine(Engine engine){
            this.engine = engine;
        }

        public void installWheel(Wheel wheel){
            this.wheels = wheel;
        }

        public void installBody(CarBody carBody){
            this.carBody = carBody;
        }

        public Car finishBuild(){
            System.out.println(ANSI_BLUE + "built a car. ID: " + id + ANSI_RESET);
            return new Car(id, engine, carBody, wheels);
        }

    }
}
