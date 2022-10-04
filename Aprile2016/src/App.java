import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 3;
        Factory fattorie[] = new Factory[n];
        for ( int i = 0; i < n ; i++){
            fattorie[i] = new Factory();
        }

        Semaphore turniA[] = new Semaphore[n];
        Semaphore turniB[] = new Semaphore[n];
        turniA[0] = new Semaphore(1);
        turniB[0] = new Semaphore(1);

        for ( int i = 1; i < turniA.length ; i++){
            turniA[i] = new Semaphore(0);
            turniB[i] = new Semaphore(0);
        }

        WorkerA[] workersA = new WorkerA[n];
        WorkerB[] workersB = new WorkerB[n];

        for ( int i = 0; i < n ; i++){
            workersA[i] = new WorkerA(fattorie,turniA);
            workersA[i].setName("[WorkerA-"+i+"]");
            workersB[i] = new WorkerB(fattorie,turniB);
            workersB[i].setName("[WorkerB-"+i+"]");
            workersA[i].start();
            workersB[i].start();
        }

    }
}

class WorkerA extends Thread{
    
    Factory factory[];

    Semaphore turniA[];

    static int ID = 0;
    int id;

    public WorkerA(Factory factory[], Semaphore[] turniA ){
        this.factory = factory;
        this.turniA = turniA;
        this.id = ID;
        ID++;
    }

    public void run(){

        try{
            turniA[id%3].acquire();
            for ( int i = 0; i < 3 ; i++){
                factory[i%3].JobA();
                System.out.println(getName()+ ": JobA eseguito su oggetto "+ (i+1));
            }

            turniA[(id+1)%3].release();
        } catch(Exception e){

        }
        

    }
}

class WorkerB extends Thread{

    Factory factory[];

    Semaphore turniB[];

    static int ID = 0;
    int id;

    public WorkerB(Factory factory[],Semaphore turniB[]){
        this.factory = factory;
        this.turniB = turniB;
        id = ID;
        ID++;

    }

    public void run(){
        try{
            turniB[id%3].acquire();

            for ( int i = 0; i < 3 ; i++){
                factory[i%3].JobB();
                System.out.println(getName()+ ": JobB eseguito su oggetto "+ (i+1));
            }
            turniB[(id+1)%3].release();
        } catch(Exception e){
            
        }
    
    }
}


class Factory{

    Factory factory[];

    public void JobA(){

    }

    public void JobB(){

    }
}