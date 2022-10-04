import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Deposito deposito = new Deposito();

        Semaphore full = new Semaphore(0); //solo per D1
        Semaphore empty = new Semaphore(10);

        Semaphore mutex1 = new Semaphore(1);

        Prodotture produttori[] = new Prodotture[3];
        produttori[0] = new Prodotture(deposito,full,empty,mutex1);
        produttori[0].setName("[P1]");
        produttori[1] = new Prodotture(deposito,full,empty,mutex1);
        produttori[1].setName("[P2]");
        produttori[2] = new Prodotture(deposito,full,empty,mutex1);
        produttori[2].setName("[P3]");

        produttori[0].start();
        produttori[1].start();
        produttori[2].start();

        Consumatore consumatore1 = new Consumatore(deposito,full,empty,mutex1);
        consumatore1.setName("[C1]");
        Consumatore consumatore2 = new Consumatore(deposito,full,empty,mutex1);
        consumatore2.setName("[C2]");
        consumatore1.start();
        consumatore2.start();


    }
}

class Prodotture extends Thread{

    int id;
    static int ID = 0;

    Deposito deposito;

    Random random = new Random();

    Semaphore full, empty;

    Semaphore mutex1;

    int n = 0;


    public Prodotture(Deposito deposito, Semaphore full, Semaphore empty, Semaphore mutex1){
        this.id = ID;
        ID++;
        this.deposito = deposito;
        this.full = full;
        this.empty = empty;
        this.mutex1 = mutex1;
    }

    public void run(){

        while(true){

            try{
                n = random.nextInt(10);
                empty.acquire(n);
                mutex1.acquire();
                System.out.println(getName() + " vorrebbe aggiungere " + n + " oggetti nel deposito D1");
                System.out.println(getName()+ " ha inserito "+ n+ " oggetti nel deposito D1");
                for ( int i =0; i < n ; i++){
                    deposito.D1.add(i);
                }
                mutex1.release();
                System.out.println(getName()+ " : Nel deposito D1 ci sono " + deposito.D1.size()+ " oggetti");
                full.release(n);
                
            } catch(Exception e){

            }
    }
    }
}

class Consumatore extends Thread{

    Deposito deposito;

    Semaphore full, empty;

    Semaphore mutex1;

    public Consumatore(Deposito deposito, Semaphore full, Semaphore empty, Semaphore mutex1){
        this.full = full;
        this.deposito = deposito;
        this.empty = empty;
        this.mutex1 = mutex1;
    }

    public void run(){
        while ( true){
            try{
                full.acquire();
                mutex1.acquire();
                System.out.println(getName() + " estrae un oggetto da D1");
                deposito.D1.removeLast();
                System.out.println(getName()+ ": Nel deposito D1 ci sono " + deposito.D1.size() + " oggetti");
                deposito.D2.add(1);
                System.out.println(getName() + " inserisce un oggetto in D2");
                mutex1.release();
                empty.release();

            } catch ( Exception e){
            }

        }
    }
}

class Deposito {

    LinkedList<Integer> D1 = new LinkedList<>();
    LinkedList<Integer> D2 = new LinkedList<>();
}