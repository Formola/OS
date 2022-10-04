import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.text.Utilities;

public class App {
    public static void main(String[] args) throws Exception {
        
        Monetina monetina = new Monetina();
        Semaphore mutex = new Semaphore(1);
        Semaphore startGame = new Semaphore(0);
        Semaphore mySem = new Semaphore(0);
        Semaphore nextSem = new Semaphore(0);

        Giocatore G1 = new Giocatore(mySem,nextSem,mutex,startGame,monetina);      //ktm passali invertiti
        Giocatore G2 = new Giocatore(nextSem,mySem, mutex,startGame,monetina);
        Arbitro arbitro = new Arbitro(mySem, nextSem, mutex,startGame,monetina);

        G1.setName("[Giocatore1]");
        G2.setName("[Giocatore2]");
        arbitro.setName("[Arbitro]");
        
        arbitro.start();
        G1.start();
        G2.start();
    }
}

class Monetina {
    int val ;
}


class Giocatore extends Thread {

    Semaphore mySem, nextSem, mutex;

    int id;
    static int ID = 0;

    Semaphore startGame;

    Monetina monetina;

    static boolean start = false;

    public Giocatore(Semaphore mySem, Semaphore nextSem, Semaphore mutex, Semaphore startGame, Monetina monetina){
        this.mySem = mySem;
        this.nextSem = nextSem;
        this.mutex = mutex;
        this.monetina = monetina;
        this.startGame = startGame;
        this.id = ID;
        ID++;
    }
     
    public void run(){
        try {
            startGame.acquire();
            mutex.acquire();
            if (start == false) {
                if (monetina.val == id) {
                    start = true;
                    mySem.release();
                } else {
                    start = true;
                    nextSem.release();
                }
                mutex.release();
                startGame.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        while ( true) {

            try {
                mySem.acquire();
                System.out.println("Sono " + getName() + " con ID=" + id + " e colpisco la palla");
                nextSem.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
}

class Arbitro extends Thread {

    Semaphore mutex;
    Random random = new Random();

    Semaphore startGame;
    Monetina monetina;

    Semaphore semA,semB;

    public Arbitro( Semaphore semA, Semaphore semB, Semaphore mutex, Semaphore startGame, Monetina monetina) {
        this.semA = semA;
        this.semB = semB;
        this.monetina = monetina;
        this.mutex = mutex;
        this.startGame = startGame;
    }

    public void run(){
        try{
            mutex.acquire();
            monetina.val = random.nextInt(2);
            //System.out.println("Monetina = " +monetina.val);
            System.out.println("Inizia il Giocatore con ID=" +(monetina.val));
            mutex.release();
            startGame.release();
        } catch (Exception e){
            System.out.println(e);
        }

    }

}