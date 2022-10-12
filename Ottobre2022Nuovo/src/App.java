import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 3;
        Semaphore mutexControllore = new Semaphore(0);
        Semaphore mutexWorker = new Semaphore(n);

        Worker[] worker = new Worker[n];
        for ( int i = 0; i <n; i++){
            worker[i] = new Worker(mutexControllore, mutexWorker);
            worker[i].setName("[Worker"+i+"]");
            worker[i].start();
        }
        Controllore controllore = new Controllore(mutexControllore, mutexWorker,worker);
        controllore.setName("[Controllore]");
        controllore.start();


    }
}

class Worker extends Thread {


    int k= 5;
    final int n = 3;
    Semaphore mutexControllore, mutexWorker;
    
    static int lavorati = 0;
    static Semaphore mutexLavorati = new Semaphore(1);

    Semaphore mySem = new Semaphore(1);


    public Worker(Semaphore mutexControllore, Semaphore mutexWorker){
        this.mutexControllore = mutexControllore;
        this.mutexWorker = mutexWorker;
    }

    public void run(){

        try{
            while(k>0){
                mySem.acquire();

                mutexWorker.acquire();
                System.out.println(getName() + " ha lavorato al pezzo " + k);

                k--;
                mutexLavorati.acquire();
                lavorati++;
                if ( lavorati == n){
                    lavorati = 0;
                    mutexLavorati.release();
                    mutexControllore.release();
                } else {
                    mutexLavorati.release();
                }
            }

        } catch( Exception e){

        }
    }
}

class Controllore extends Thread{

    Semaphore mutexControllore, mutexWorker;
    static int k = 5;
    final int n = 3;
    Worker[] worker;

    public Controllore(Semaphore mutexControllore, Semaphore mutexWorker,Worker[] worker){
        this.mutexControllore = mutexControllore;
        this.mutexWorker = mutexWorker;
        this.worker = worker;
    }

    public void run(){

        try{
            while(k>0){
                k--;
                
                if ( k > 0){
                    mutexControllore.acquire();
                    System.out.println(getName() + " fa ripartire il turno");
                    
                    for ( int i  = 0; i < n; i++){
                        worker[i].mySem.release();
                    }
                    mutexWorker.release(n);
                }


                if ( k == 0){
                    for ( int i  = 0; i < n; i++){
                        worker[i].mySem.release();
                    }
                    mutexWorker.release(n);

                    mutexControllore.release();
                }


            }

        } catch( Exception e){

        }
    }

}
