package ru.nsu.fit.daria.carfactory;

import ru.nsu.fit.daria.carfactory.products.Product;

import java.util.logging.Logger;

public class Consumer<T extends Product> extends ManagedThread{
    Storage<T> s;
    long pause;
    private static final Logger logger = Logger.getLogger(Consumer.class.getName());

    Consumer(Storage<T> s, long pause){
        this.s = s;
        this.pause = pause;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(pause);
                Object o = s.get();
                logger.info(Thread.currentThread().getName() + " :: GOT DETAIL" + o);
                if (!keepRunning()){
                    break;
                }
            } catch (InterruptedException e) {
                logger.info(Thread.currentThread().getName() + " :: INTERRUPTED");
                break;
            }
        }
    }
}
