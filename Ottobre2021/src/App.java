import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        final int N = 5;
        Worker[] workers = new Worker[N];
        Turno sportello = new Turno();

        Semaphore[] turni = new Semaphore[N];
        for ( int i = 0; i < N; i++){
            turni[i] = new Semaphore(0);
        }

        for ( int i = 0; i < N; i++){
            workers[i] = new Worker(turni,sportello);
            workers[i].start();
        }
        
    }
}

class Worker extends Thread {

    final int N = 5;
    Semaphore[] turni;
    static Semaphore mutex = new Semaphore(1); 

    int id;
    static int ID =0;

    Turno sportello;
    int myTurn;

    static int k =  2;
    static int numworkers = 0;


    public Worker(Semaphore[] turni, Turno sportello){
        this.turni = turni;
        this.id = ID;
        ID++;
        this.sportello = sportello;

    }

    public void run(){
        try{
            mutex.acquire();
            myTurn = sportello.get(id);
            System.out.println(getName() + " e sono arrivato " + myTurn);
            numworkers++;
            if ( numworkers == N){
                System.out.println("Ci sono "+N+" Thread e "+k+" operazioni , quindi in totale ci aspettiamo vengano eseguite "+(k*N)+" operazioni");
                turni[0].release();
            } else { mutex.release();
            }

            for ( int i = 0 ; i < k ; i++){
                turni[myTurn].acquire();
                System.out.println(getName() + " e eseguo un operazione, ne rimangono " +(k-i));
                turni[(myTurn+1)%N].release();
            }

            


        } catch(Exception e){
            System.out.println(e);
        }

    }
}

class Turno {

    int ordine[] = {3, 4,0,1 ,2};

    public int get(int i){
        return ordine[i];
    }
}
