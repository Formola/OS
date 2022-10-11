import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.border.EmptyBorder;;

public class App {
    public static void main(String[] args) throws Exception {

        Deposito deposito = new Deposito();

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(1);
        Semaphore emptyB = new Semaphore(5); // quanti ne puo avere
        Semaphore fullB = new Semaphore(0);  //quanti ce ne sono


        P p1 = new P(deposito,mutexA,emptyB,fullB,mutexB);
        p1.setName("[P1]");
        P p2 = new P(deposito,mutexA, emptyB, fullB,mutexB);
        p2.setName("[P2]");
        C c1 = new C(deposito, mutexA, emptyB, fullB,mutexB);
        c1.setName("[C]");
        p1.start();
        p2.start();
        c1.start();


    }
}

class P extends Thread {

    Deposito deposito;
    Semaphore mutexA;
    Semaphore emptyB;
    Semaphore fullB;
    Semaphore mutexB;

    public P(Deposito deposito,Semaphore mutexA, Semaphore emptyB, Semaphore fullB,Semaphore mutexB){
        this.deposito = deposito;
        this.mutexA = mutexA;
        this.emptyB = emptyB;
        this.fullB = fullB;
        this.mutexB = mutexB;
    }

    public void run(){
        try{
            while(deposito.depositoA.size()>0){

                mutexA.acquire();
                Object object = deposito.estraiA();
                mutexA.release();

                emptyB.acquire();
                mutexB.acquire();
                deposito.inserisciB();
                System.out.println(getName() + " ha estratto un oggetto da A e lo ha inserito in B");
                mutexB.release();
                fullB.release();


                if ( deposito.depositoA.size() == 0){
                    System.out.println("Terminati oggetti nel depositoA");
                    break;
                }


            }
        } catch(Exception e){

        }
    }
}

class C extends Thread{

    Deposito deposito;
    Semaphore mutexA;
    Semaphore emptyB;
    Semaphore fullB;
    Semaphore mutexB;

    public C (Deposito deposito, Semaphore mutexA, Semaphore emptyB, Semaphore fullB ,Semaphore mutexB){
        this.deposito = deposito;
        this.mutexA = mutexA;
        this.emptyB = emptyB;
        this.fullB = fullB;
        this.mutexB = mutexB;
    }

    public void run(){

        try{

            while(deposito.depositoB.size()>=0 && deposito.depositoA.size()>= 0){
                fullB.acquire();
                mutexB.acquire();
                deposito.estraiB();
                deposito.consuma();
                System.out.println(getName() + " ha consumato un oggetto dal deposito B, ora ce ne sono " + deposito.depositoB.size());
                mutexB.release();
                emptyB.release();

                if ( deposito.depositoA.size() == 0 && deposito.depositoB.size()==0){
                   break;
                }
            }
            
        } catch(Exception e){

        }

    }
}

class Deposito {

    LinkedList<Object> depositoA = new LinkedList<>();
    LinkedList<Object> depositoB = new LinkedList<>();

    public Deposito(){
        for ( int i = 0; i < 100; i++){
            depositoA.add(new Object()); //dice infinito all'inizio
        }
    }

    public Object estraiA(){
        return depositoA.removeLast();
    }

    public void inserisciB(){
        depositoB.add(new Object());
    }

    public Object estraiB(){
        return depositoB.removeLast();
    }

    public void consuma(){

    }
}