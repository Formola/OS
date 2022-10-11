import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        LinkedList<Integer> Ram = new LinkedList<>();

        final int k = 3;
        final int m = 2;

        Semaphore mutexScrittura = new Semaphore(1);
        Semaphore mutexLettura = new Semaphore(0);


        Scrittore[] scrittori = new Scrittore[k];
        for ( int i = 0; i < k; i++){
            scrittori[i] = new Scrittore(Ram,mutexScrittura,mutexLettura);
            scrittori[i].setName("[Scrittore"+i+"]");
            scrittori[i].start();
        }

        Lettore[] lettori = new Lettore[m];
        for ( int i = 0; i< m; i++){
            lettori[i] = new Lettore(Ram,mutexScrittura,mutexLettura);
            lettori[i].setName("[Lettore"+i+"]");
            lettori[i].start();
        }
        

    }
}

class Scrittore extends Thread {


    LinkedList<Integer> Ram;
    Semaphore mutexScrittura;
    Semaphore mutexLettura;

    public Scrittore(LinkedList<Integer> Ram,Semaphore mutexScrittura,Semaphore mutexLettura){
        this.Ram = Ram;
        this.mutexLettura = mutexLettura;
        this.mutexScrittura = mutexScrittura;
    }

    public void run(){

        try{

            while(true){

                mutexScrittura.acquire();
                Ram.add(1);
                System.out.println(getName() + " sta scrivendo");
                mutexLettura.release(3);

            }
        } catch( Exception e){

        }

    }
}

class Lettore extends Thread {

    LinkedList<Integer> Ram;
    Semaphore mutexScrittura;
    Semaphore mutexLettura;

    static int letture = 0;
    static Semaphore mutex = new Semaphore(1);

    public Lettore(LinkedList<Integer> Ram,Semaphore mutexScrittura,Semaphore mutexLettura){
        this.Ram = Ram;
        this.mutexLettura = mutexLettura;
        this.mutexScrittura = mutexScrittura;
    }

    public void run(){

        try{

            while(true){
                mutexLettura.acquire();
                System.out.println(getName() + " sta leggendo");

                mutex.acquire();
                letture++;
                if ( letture == 3){
                    mutexScrittura.release();
                    letture = 0;
                }
                mutex.release();
            }
        } catch( Exception e){

        }

    }
}

