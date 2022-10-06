import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Dep deposito = new Dep();

        Semaphore mutex = new Semaphore(1); // nons erve full e empty tanto rispetto sempre i vincoli

        Semaphore mutexReset = new Semaphore(0);


        Reset reset = new Reset(mutex,  mutexReset, deposito);
        reset.setName("[Reset]");
        reset.start();

        Produttore[] producers = new Produttore[3];
        for ( int i = 0; i < 3; i++){
            producers[i] = new Produttore(deposito, mutex , mutexReset);
            producers[i].setName("[Produttore"+i+"]");
            producers[i].start();
        }
        
    }


}


class Produttore extends Thread{

    Dep dep;
    int id;
    static int ID = 0;

    Semaphore  mutexReset;
    Semaphore mutex;
    static int inserimenti = 0;

    public Produttore(Dep deposito, Semaphore mutex , Semaphore mutexReset){
        this.dep = deposito;
        this.id = ID;
        ID++;

        this.mutex = mutex;
        this.mutexReset = mutexReset;
    }

    public void run(){

        try{
            while ( true){
                mutex.acquire();
                System.out.println(getName()+ " inserisce un oggetto nel deposito");
                dep.deposito.add(1);
                inserimenti++;

                if ( inserimenti  == 5){
                    inserimenti = 0;
                    mutexReset.release();
                } else {
                    mutex.release();
                }
            }

        } catch(Exception e){
        }
    }
}

class Reset extends Thread {

    Semaphore mutex,  mutexReset;

    Dep dep;

    public Reset(Semaphore mutex , Semaphore mutexReset, Dep deposito){
        this.mutexReset = mutexReset;
        this.mutex = mutex;
        this.dep = deposito;


    }

    public void run(){
        try{
            while(true){
                mutexReset.acquire();
                dep.svuota();
                System.out.println(getName() + " ha svuotato il deposito");
                mutex.release();
            }


        } catch(Exception e){

        }
    }
}

class Dep {

    LinkedList<Integer> deposito = new LinkedList<>();
    public Dep(){
        for ( int i = 0; i < 3; i++){
            deposito.add(i);
        }
    }

    public void svuota(){
        deposito.clear();
    }
}