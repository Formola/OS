import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.WindowConstants;

public class App {
    public static void main(String[] args) throws Exception {


        Semaphore mutexGiudice = new Semaphore(0);
        Semaphore mutexRunner = new Semaphore(0);
        Giudice giudice = new Giudice(mutexGiudice, mutexRunner);

        

        Runner runner1 = new Runner(mutexGiudice,mutexRunner, giudice);
        Runner runner2 = new Runner(mutexGiudice, mutexRunner, giudice);
        runner1.setName("[Runner1]");
        runner2.setName("[Runner2]");

        giudice.setName("[Giudice]");

        runner1.start();
        runner2.start();
        giudice.start();

    }
}

class Runner extends Thread {

    int id;
    static int ID = 0;

    Semaphore mutexGiudice;
    Semaphore mutexRunner;
    Giudice giudice;

    static int k = 100;


    public Runner(Semaphore mutexGiudice, Semaphore mutexRunner, Giudice giudice){
        this.id = ID;
        ID++;
        this.mutexGiudice = mutexGiudice;
        this.mutexRunner = mutexRunner;
        this.giudice = giudice;

        if ( ID == 2) {
            mutexGiudice.release();
        }
    }

    public void run(){

        try {

            mutexRunner.acquire();

            for ( int i = 0; i < k ; i++){
                System.out.println(getName() + " compie operazione " + i);

                if ( i == k-1 && giudice.winner == "" ){
                    giudice.winner = this.getName();
                    mutexGiudice.release();
                }
            }


        } catch( Exception e){

        }

    }
}

class Giudice extends Thread {

    Semaphore mutexGiudice;
    Semaphore mutexRunner;

    int winner_id = -1;
    String winner = "";

    public Giudice(Semaphore mutexGiudice, Semaphore mutexRunner){
        this.mutexGiudice = mutexGiudice;
        this.mutexRunner = mutexRunner;
    }

    public void run(){

        try{
            mutexGiudice.acquire();
            System.out.println(getName() + " da il via alla gara");
            mutexRunner.release(2);

        } catch( Exception e){

        }

        try{
            mutexGiudice.acquire();
            System.out.println(getName() + " annuncia che il vincitore e' : " + winner);



        } catch(Exception e){

        }

    }
}