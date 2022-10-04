import java.util.LinkedList;
import java.lang.Thread;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(1);
        A depositoA = new A();
        B depositoB = new B();


        P producer1 = new P ("P1",depositoA, depositoB, mutexA, mutexB);
        P producer2 = new P ("P2" ,depositoA, depositoB, mutexA, mutexB);
        C consumer = new C("C",depositoB, mutexB);

        

        consumer.start();
        producer1.start();
        producer2.start();

        consumer.sleep(1000);
        producer1.sleep(1000);
        producer2.sleep(1000);

        consumer.interrupt();
        producer1.interrupt();
        producer2.interrupt();

        System.out.println("Traccia maggio2022");


        
    }
}


class A {
    public Object estraiOggetto() {
        return new Object();
    }
}

class B {
    public Semaphore full;
    public Semaphore empty;
    public LinkedList<Object> items;

    B(){
        full = new Semaphore(0);
        empty = new Semaphore(5);
        items = new LinkedList<Object>();
    }

    public void inserisciOggetto(Object obj){
        items.push(obj);
    }

    public Object removeOggetto(){
        return items.removeFirst();
    }
}

class C extends Thread {
    public B depositoB;
    public Semaphore mutexB;
    int id;
    static int ID = 0;
    
    C (String name, B depositoB , Semaphore mutexB){
        this.depositoB = depositoB;
        this.mutexB = mutexB;
        id = ID++;
        setName(name);
    }

    public void consuma(Object obj){
        System.out.println(obj.toString() + "consumato");
    }

    public void run(){
        try{
            while(true){

                depositoB.full.acquire();
                System.out.println("superato semaforo full");
                mutexB.acquire();
                Object obj = depositoB.removeOggetto();
                consuma(obj);
                System.out.println(getName() + " ha appena consumato un oggetto!");
                mutexB.release();
                depositoB.empty.release();

            }

        } catch (Exception e){
            System.out.println(getName() + e);
        }
    }
}

class P extends Thread {
    public A depositoA;
    public B depositoB;

    public Semaphore mutexA;
    public Semaphore mutexB;

    Object oggettoEstratto;
    int id;
    static int ID = 0;

    public P (String name, A depositoA, B depositoB, Semaphore mutexA, Semaphore mutexB){
        this.depositoA = depositoA;
        this.depositoB = depositoB;
        this.mutexA = mutexA;
        this.mutexB = mutexB;
        id = ID++;

        setName(name);;
    }
    
    public void run(){
        String threadName = getName();
        try{
            while(true){
                mutexA.acquire();
                System.out.println(threadName + " semaforo A rosso");
                this.oggettoEstratto = depositoA.estraiOggetto();
                System.out.println(threadName + " ha appena estratto un oggetto dal deposito A");
                mutexA.release();
                System.out.println(threadName + " semaforo A verde");

                depositoB.empty.acquire();
                System.out.println(threadName + " ha superato il semaforo empty di B");
                mutexB.acquire();
                depositoB.inserisciOggetto(this.oggettoEstratto);
                System.out.println(threadName + " ha appena inserito oggetto nel deposito B");
                mutexB.release();
                depositoB.full.release();
                System.out.println("Stato semafori B: full: " + depositoB.full.availablePermits() + " empty: " + depositoB.empty.availablePermits());
            }

        } catch(Exception e){
            System.out.println(getName() + e);
        }
    }
}