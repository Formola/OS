import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 5;
        Factory fattoria = new Factory();
        Turni turno = new Turni(n);
        turno.fillTurni();

        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        Semaphore mutex = new Semaphore(1);


        Worker[] workers = new Worker[n];

        for ( int i = 0; i < n ; i++){
            workers[i] = new Worker(fattoria, turno, turni, mutex);
            workers[i].setName("[Worker"+i+"]");
            workers[i].start();
        }
    }
}

class Worker extends Thread {

    Factory factory;
    Turni turno;

    final int n = 5;

    int id;
    static int ID = 0;

    Semaphore[] turni;

    Semaphore mySem, nextSem, mutex;

    static Random random = new Random();
    static int k  = random.nextInt(10)+1;

    int ordine = 0;
    static int i = 0;

    public Worker(Factory factory, Turni turno, Semaphore[] turni, Semaphore mutex){
        this.factory = factory;
        this.id = ID;
        ID++;
        this.turni = turni;
        this.turno = turno;
        this.mutex = mutex;

    }

    public void run(){
        System.out.println(getName() + " dovra' eseguire " + k + " operazioni");
        try{
            mutex.acquire();
            ordine = turno.turni.get(i);
            i++;
            System.out.println(getName() + " ha rollato ordine " + ordine);
            mutex.release();

            if(i == n-1){
                turni[0].release();
            }

            for ( int i  = 0; i < k ; i++){
                turni[ordine].acquire();
                System.out.println("Sono " + getName() + " ed eseguo un job sull fattoria");


                if ( k == 0){
                    System.out.println(getName() + " ha finito");
                    turni[(ordine+1)%n].release();
                } else{
                    turni[(ordine+1)%n].release();
                }

                if ( ordine == (n-1)){
                    System.out.println("Rimangono " + (k-i-1) +" operazioni da effettuare");

                }

                }


            
        } catch(Exception e){

        }



    }
}

class Factory{

    public void Job(){

    }
}

class Turni {

    int n;

    LinkedList<Integer> turni = new LinkedList<>();

    public Turni(int n){
        this.n = n;
    }
    public void fillTurni(){
        for ( int i = 0; i < n; i++){
            turni.add(i);
        }
        Collections.shuffle(turni);
    }
}