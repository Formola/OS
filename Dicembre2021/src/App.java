import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int N = 3;
        Semaphore[] turni = new Semaphore[N];
        Semaphore coordinatorSemaphor = new Semaphore(1);

        Worker[] workers = new Worker[N];

        for ( int i = 0 ; i < N; i++){
            turni[i] = new Semaphore(0);
        }

        for ( int i = 0; i < N ; i++){
            workers[i] = new Worker(turni , coordinatorSemaphor);
        }

        Coordinatore coordinatore = new Coordinatore(turni,  coordinatorSemaphor, N);

        coordinatore.start();
        for( int i =0; i<N; i++){
            workers[i].start();
        }

        System.out.println("test");

        

    }
}

class Worker extends Thread {

    Semaphore[] turni;
    Semaphore coordinatorSemaphore;
    int id;
    static int ID = 0;

    public Worker(Semaphore[] turni, Semaphore coordinatorSemaphore){
        id = ID;
        ID++;
        this.turni = turni;
        this.coordinatorSemaphore = coordinatorSemaphore;
    }

    public void run(){
        while(true){
            try{
                turni[id].acquire();
                System.out.println("["+getName()+"] : operazione eseguita");
                coordinatorSemaphore.release();
                //this.sleep(5000); //debuggin abusivo
            } catch(Exception e){
                System.out.println(e);
            }

        }

    }

}

class Coordinatore extends Thread {


    Semaphore[] turni;
    Random random = new Random();
    Semaphore coordinatorSemaphore;
    int n;

    public Coordinatore(Semaphore[] turni , Semaphore coordinatorSemaphore, int n){
        this.turni = turni;
        this.coordinatorSemaphore = coordinatorSemaphore;
        this.n = n;
    }

    public void run(){
        while(true){
            try{
                coordinatorSemaphore.acquire();
                int randomInt = random.nextInt(n);
                turni[randomInt].release();
                System.out.println("[Coordinatore] : adesso tocca al thread: "+ randomInt);
            } catch(Exception e){
                System.out.println(e);
            }
        }

    }

}
