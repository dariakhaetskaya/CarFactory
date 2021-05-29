package ru.nsu.fit.daria.carfactory.products;

import ru.nsu.fit.daria.carfactory.spares.CarBody;
import ru.nsu.fit.daria.carfactory.spares.Engine;
import ru.nsu.fit.daria.carfactory.spares.Wheel;
import ru.nsu.fit.daria.carfactory.tasks.Supply;

import java.util.logging.Logger;

import static ru.nsu.fit.daria.carfactory.util.Utils.*;

public class Car extends Product{
    private final Engine engine;
    private final CarBody carBody;
    private final Wheel wheels;
    private static final Logger logger = Logger.getLogger(Car.class.getName());

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
            logger.info("BUILD A CAR. ID: " + id);
            return new Car(id, engine, carBody, wheels);
        }

    }
}
