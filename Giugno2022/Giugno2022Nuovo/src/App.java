import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 5;
        Semaphore[] turni = new Semaphore[n];

        Semaphore mutexCapo = new Semaphore(0);
        for ( int i = 0 ; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        Caporeparto capo = new Caporeparto(turni, mutexCapo);

        Worker worker[] = new Worker[n];
        for ( int i  = 0; i < n ; i++){
            worker[i] = new Worker(turni, mutexCapo);
            worker[i].setName("[Worker"+i+"]");
            worker[i].start();
        }
        capo.setName("[Caporeparto]");
        capo.start();

    }
}

class Worker extends Thread {

    final static int n = 5;
    Semaphore[] turni;
    Semaphore mutexCapo;

    int id ;
    static int ID = 0;

    static Semaphore mutexOrdine = new Semaphore(1);
    static int ordine = 0;

    Semaphore mySem;

    public Worker(Semaphore[] turni, Semaphore mutexCapo){
        this.turni = turni;
        this.mutexCapo = mutexCapo;

    }

    public void run(){

        try {
            mutexOrdine.acquire();
            System.out.println(getName() + " e' arrivato " + (ordine+1));
            mySem = turni[ordine];
                if ( ordine == n-1 ){
                    System.out.println("Ordini acquisiti, sveglio il boss");
                    mutexCapo.release();
                } else {}
            ordine++;
            mutexOrdine.release();

                        
            mySem.acquire();
            System.out.println(getName() + " esegue in ME un attivita'");
            mutexCapo.release();
        } catch(Exception e){
        }


    }
}

class Caporeparto extends Thread {

    Semaphore mutexCapo;
    Semaphore[] turni;

    int ordine = 0;


    public Caporeparto(Semaphore[] turni, Semaphore mutexCapo){
        this.turni = turni;
        this.mutexCapo  = mutexCapo;

    }

    public void run(){

        
            

            try{
                mutexCapo.acquire();
                System.out.println(getName() + " e attivo il worker successivo");
                turni[ordine].release();
                ordine++;
                

            } catch(Exception e){

            }
        

    }
}