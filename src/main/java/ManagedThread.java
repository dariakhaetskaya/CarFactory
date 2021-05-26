import ru.nsu.fit.daria.carfactory.util.ThreadState;
import static ru.nsu.fit.daria.carfactory.util.ThreadState.*;

public class ManagedThread extends Thread {
    private ThreadState desiredState = RUNNING;
    private final Object monitor = new Object();

    protected synchronized ThreadState getDesiredState(){
        return desiredState;
    }

    public void mstop(){
        synchronized (monitor){
            desiredState = STOP;
            monitor.notifyAll(); // required to stop sleeping threads
        }
    }

    public void msuspend(){
        synchronized (monitor){
            // stopped threads cannot be suspended because STOP is a final state
            if (desiredState != STOP) {
                desiredState = SLEEP;
            }
        }
    }

    public void mresume(){
        synchronized (monitor){
            // only sleeping threads can be resumed
            if (desiredState == SLEEP) {
                desiredState = RUNNING;
                monitor.notifyAll();
            }
        }
    }

    protected boolean keepRunning(){
        synchronized (monitor){
            if (isInterrupted()){
                System.out.println(Thread.currentThread().getName()+" was interrupted by flag");
                return false;
            }

            if (desiredState == RUNNING){
                return true;
            } else {
                while (true){
                    if (isInterrupted()){
                        System.out.println(Thread.currentThread().getName()+" was interrupted by flag");
                        return false;
                    } else if (desiredState == STOP){
                        System.out.println(Thread.currentThread().getName()+" was stopped");
                        return false;
                    } else if (desiredState == SLEEP){
                        try {
                            System.out.println(Thread.currentThread().getName()+" fell asleep");
                            monitor.wait();
                        } catch (InterruptedException e) {
                            System.out.println(Thread.currentThread().getName()+" interrupted");
                            return false;
                        }
                    } else if (desiredState == RUNNING){
                        System.out.println(Thread.currentThread().getName()+" resumed");
                        return true;
                    }
                }
            }
        }
    }
}
