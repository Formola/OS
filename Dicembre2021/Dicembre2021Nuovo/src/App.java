import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 6;
        Semaphore turni[] = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        Semaphore mutexCoordinatore = new Semaphore(0);
        Coordinatore coordinatore = new Coordinatore(turni,mutexCoordinatore);
        coordinatore.setName("[Coordinatore]");
        coordinatore.start();
        Worker worker[] = new Worker[n];
        for ( int i = 0; i < n; i++){
            worker[i] = new Worker(turni,mutexCoordinatore);
            worker[i].setName("[Worker"+i+"]");
            worker[i].start();
        }


    }
}

class Worker extends Thread {

    final int n = 6;
    Semaphore turni[];
    int id;
    static int ID = 0;
    Semaphore mutexCoordinatore;

    public Worker(Semaphore turni[], Semaphore mutexCoordinatore){
        this.turni = turni;
        this.id = ID;
        ID++;
        this.mutexCoordinatore = mutexCoordinatore;

    }

    public void run(){

        try{

            while(true){
                turni[id].acquire();
                System.out.println(getName() + " esegue un operazione in mutua esclusione");
                mutexCoordinatore.release();
            }
        } catch(Exception e){

        }

    }
}

class Coordinatore extends Thread {

    final int n = 6;
    Semaphore turni[];
    Random random = new Random();
    int choosen_id = -1;
    Semaphore mutexCoordinatore;

    public Coordinatore(Semaphore turni[],Semaphore mutexCoordinatore){
        this.turni = turni;
        choosen_id = random.nextInt(n);
        turni[choosen_id].release();
        this.mutexCoordinatore = mutexCoordinatore;
    }

    public void run(){
        try{
            while(true){
                mutexCoordinatore.acquire();
                choosen_id = random.nextInt(n);
                System.out.println(getName() + " comunica che il prossimo worker a lavorare sarà : Worker"+choosen_id);
                turni[choosen_id].release();
            }

        } catch(Exception e){

        }
    }
}