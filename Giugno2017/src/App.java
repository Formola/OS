import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 5;

        Coda coda = new Coda();

        Semaphore mutexStart = new Semaphore(1);

        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n ; i++){
            turni[i] = new Semaphore(0);
        }




        Produttore[] produttori = new Produttore[n];
        for ( int i = 0; i < n; i++){
            produttori[i] = new Produttore(coda, mutexStart, turni);
            produttori[i].setName("[Produttore"+i+"]");
            produttori[i].start();
        }
    }
}

class Produttore extends Thread{

    Coda coda;
    final int n = 5;

    int k = 2;
    int id;
    static int ID = 0;

    Semaphore[] turni;
    Semaphore mutexStart, mySem, nextSem;

    static int ordine = 0;


    public Produttore(Coda coda, Semaphore mutexStart, Semaphore[] turni){
        this.coda = coda;
        this.id = ID;
        ID++;
        this.mutexStart = mutexStart;
        this.turni = turni;

    }

    public void run(){
        try{
            mutexStart.acquire();
            if ( ordine == 0){
                System.out.println(getName() + " accede per primo alla regione critica, inzia lui il giro");
            } else {
                System.out.println(getName() + " accede per " + (ordine+1));
            }
            mySem = turni[ordine];
            nextSem = turni[(ordine+1)%n];
            ordine++;
            if ( ordine == n-1){
                turni[0].release();
            }
            mutexStart.release();

            for ( int i = 0; i < k; i++){
                mySem.acquire();
                coda.coda.add(1);
                System.out.println(getName() + " ha aggiunto 1 elemento alla coda");
                nextSem.release();

                if ( i == k-1){
                    System.out.println(getName() + " ha terminato le operazioni");
                }
            }

        } catch(Exception e){

        }

    }

}

class Coda {

    LinkedList<Integer> coda = new LinkedList<>();

}