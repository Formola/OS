import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Semaphore mutexBianca = new Semaphore(1);
        Semaphore mutexNera= new Semaphore(0);

        Semaphore[] turniA = new Semaphore[2];
        Semaphore[] turniB = new Semaphore[2];
        turniA[0] = new Semaphore(1);
        turniA[1] = new Semaphore(0);
        turniB[0] = new Semaphore(0);
        turniB[1] = new Semaphore(0);
        
        
        B B1 = new B(mutexBianca,mutexNera,turniA);
        B1.setName("[B1]");
        B B2 = new B(mutexBianca,mutexNera,turniA);
        B2.setName("[B2]");

        N N1 = new N(mutexNera,mutexBianca,turniB);
        N1.setName("[N1]");
        N N2 = new N(mutexNera,mutexBianca,turniB);
        N2.setName("[N2]");

        B1.start();
        B2.start();
        N1.start();
        N2.start();
    }
}

class B extends Thread{

    int id;
    static int ID = 0;

    Semaphore mutexBianca;
    Semaphore mutexNera;

    static int colpi = 0;

    Semaphore[] turniBianca;


    public B(Semaphore mutexBianca, Semaphore mutexNera, Semaphore[] turniBianca){
        this.id = ID;
        ID++;
        this.mutexBianca = mutexBianca;
        this.mutexNera = mutexNera;
        this.turniBianca = turniBianca;


    }

    public void run(){
        
        try{
            while(colpi<10){
                turniBianca[id].acquire();
                mutexBianca.acquire();
                System.out.println("Sono " + getName() + " della squadra bianca e ho colpito la pallina");
                colpi++;
                mutexNera.release();
                turniBianca[(id+1)%2].release();
            }


        } catch(Exception e){

        }
    }
}

class N extends Thread{

    int id;
    static int ID = 0;
    
    static int colpi = 0;
    static Semaphore mutex = new Semaphore(1);

    Semaphore mutexNera;
    Semaphore mutexBianca;

    Semaphore[] turniNera;


    public N(Semaphore mutexNera, Semaphore mutexBianca, Semaphore[] turniNera){
        this.id = ID;
        ID++;
        this.mutexNera = mutexNera;
        this.mutexBianca = mutexBianca;
        this.turniNera = turniNera;
    }

    public void run(){
        try{
            if ( id == 0) {
                turniNera[0].release();
            } 

            while(colpi<10){
                turniNera[id].acquire();
                mutexNera.acquire();
                System.out.println("Sono " + getName() + " della squadra nera e ho colpito la pallina");
                colpi++;
                mutexBianca.release();
                turniNera[(id+1)%2].release();
            }
            
        } catch (Exception e){

        }
    }
}