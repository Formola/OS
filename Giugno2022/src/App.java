import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int N = 5;
        Semaphore[] turni = new Semaphore[N];
        Semaphore capo = new Semaphore(0);

        for ( int i = 0; i < N ; i++){
            turni[i] = new Semaphore(0);
        }

        Worker worker[] = new Worker[N];
        CapoReparto cap = new CapoReparto(turni,capo);
        cap.start();

        for ( int i = 0; i < N ; i++){
            worker[i] = new Worker(turni, capo);
            worker[i].start();
        }
    }

}

class Worker extends Thread {
    
    Semaphore turni[];
    Semaphore capo;

    int id;
    static int ID = 0, N = 5, ordine = 0;

    Semaphore mySem;
    static Semaphore mutex = new Semaphore(1); //lo uso per ordine

    public Worker(Semaphore[] turni, Semaphore capo){
        this.turni = turni;
        this.capo = capo;
        id = ID++;
    }

    public void run (){

        try{
            mutex.acquire();
            System.out.println("Sono " + getName() + " con ID=" + id + "e sono arrivato "+(ordine+1));

            mySem = turni[ordine];
            if ( ordine == N-1){
                System.out.println("Ordini acquisiti, sveglio il capo");;
                capo.release();
            } else {}
            ordine++;
            mutex.release();

            mySem.acquire();   
            System.out.println("Sono " + getName() + " e sto eseguendo l'attività");
            capo.release();

            
        } catch(Exception e){
            System.out.println(e);
        }

    }
}

class CapoReparto extends Thread {

    Semaphore[] turni;
    Semaphore capo;
    int ordine = 0;


    public CapoReparto(Semaphore[] turni, Semaphore capo){
        this.turni = turni;
        this.capo = capo;
    }

    public void run(){
        try{
            while(ordine<Worker.N){
                capo.acquire();
                turni[ordine].release();
                ordine++;
            }
        } catch(Exception e){
            System.out.println(e);
        }

    }
}
