import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int n = 5;
        Sportello sportello = new Sportello(n);

        Semaphore turni[] = new Semaphore[n];
        for (int i = 0; i < turni.length; i++) {
            turni[i]=new Semaphore(0);
        }


        Worker[] worker = new Worker[n];
        for ( int i =0; i < n; i++){
            worker[i] = new Worker(sportello, turni);
            worker[i].setName("[Worker"+i+"]");
            worker[i].start();
        }
    }
}

class Worker extends Thread {

    Sportello sportello;
    final int n = 5;
    int id;
    static int k = 20;
    static Semaphore mutex = new Semaphore(1);
    static int ordine = 0;

    int mioTurno;

    Semaphore turni[];

    public Worker(Sportello sportello,Semaphore turni[]){
        this.sportello = sportello;
        this.turni = turni;



    }

    public void run(){

        try{
            mutex.acquire();
            id = ordine++;
            mioTurno = sportello.turno.get(id);
            System.out.println(getName() + " ha rollato turno " +mioTurno);
            if ( ordine == n){
                turni[0].release();
            } 
            mutex.release();

            for ( int i = 0; i < k ; i++){
                turni[mioTurno].acquire();
                System.out.println("Sono " + getName()+ " con ID= " + id + " e turno " + mioTurno + " e sto eseguendo l'operazione " + i);
                turni[(mioTurno+1)%n].release();
            }
            

        } catch( Exception e){

        }
    }
}

class Sportello {

    LinkedList<Integer> turno = new LinkedList<>();
    int n;

    public Sportello(int n){
        this.n = n;

        for ( int i  =0; i<n; i++){
            turno.add(i);
        }
        Collections.shuffle(turno);
    }
}

