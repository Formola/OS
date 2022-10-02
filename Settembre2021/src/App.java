import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int N = 3;

        DepositoA depA = new DepositoA();
        DepositoB depB = new DepositoB();

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(1);

        Robot robots[] = new Robot[N];
        for ( int i = 0; i < N ; i++){
            robots[i] = new Robot(depA,depB, mutexA, mutexB);
            robots[i].setName("[Robot-"+i+"]");
            robots[i].start();
        }

    }
}


class Robot extends Thread {

    static Queue<Integer> coda = new LinkedList<Integer>();
    static Semaphore mutexqueue = new Semaphore(1);

    static int k = 50; //iterazioni

    DepositoA depositoA;
    DepositoB depositoB;

    Semaphore mutexA, mutexB;

    int id;
    static int ID = 0;

    static int index = 0;
    Semaphore mutexIndex = new Semaphore(1);

    

    public Robot(DepositoA depA, DepositoB depB, Semaphore mutexA, Semaphore mutexB){
        this.id = ID;
        ID++;
        this.depositoA = depA;
        this.depositoB = depB;
        this.mutexA = mutexA;
        this.mutexB = mutexB;

        if ( ID == 2){
            for ( int i = 0; i < (k*3)+50; i++){
                coda.add(i);
            }
        }

    }

    public void run(){
        
        try{
            System.out.println("Sono "+getName());
            for ( int i = 1; i < k+1 ; i++){
                mutexqueue.acquire();
                coda.remove();
                mutexqueue.release();
                try{
                    mutexIndex.acquire();
                    index = i;
                    if ( i%2 == 0) {
                        mutexB.acquire();
                        depositoB.depB.add(i);
                        System.out.println(getName()+ " : ho estratto un oggetto pari e lo metto nel deposito B");
                        mutexB.release();
                    } else {
                        mutexA.acquire();
                        depositoA.depA.add(i);
                        System.out.println(getName()+ " : ho estratto un oggetto dispari e lo metto nel deposito A");
                        mutexA.release();
                    }
                    mutexIndex.release();
                } catch( Exception e){
                    System.out.println(e);
                }
                
            }

            System.out.println("Oggetti rimasti nella coda : " +coda.size() + " \nLa traccia dice coda illimitata bro");
        } catch(Exception e){
            System.out.println(e);
        }
    }
}

class DepositoA {

    Queue<Integer> depA = new LinkedList<Integer>();

    public void inserisci(){
        depA.add(1);
    }
}

class DepositoB{

    Queue<Integer> depB = new LinkedList<Integer>();

    public void inserisci(){
        depB.add(1);
    }
}