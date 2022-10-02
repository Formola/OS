import java.util.*;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {
        
        LinkedList<Integer> depA = new LinkedList<>(); //inizialmente 10 oggetti dentro
        LinkedList<Integer> depB = new LinkedList<>();
        for ( int i = 0; i < 10; i++){
            depA.add(i);
        }
        DepositoA depositoA = new DepositoA(depA);
        DepositoB depositoB = new DepositoB(depB);

        Semaphore full = new Semaphore(10);
        Semaphore empty = new Semaphore(0);
    
        Semaphore[] turni = new Semaphore[2];
        turni[0] = new Semaphore(0);
        turni[1] = new Semaphore(0);


        Robot robot1 = new Robot(depositoA, depositoB, full, empty, turni);
        robot1.setName("[Robot1]");
        Robot robot2 = new Robot(depositoA, depositoB, full, empty, turni);
        robot2.setName("[Robot2]");

        robot1.start();
        robot2.start();

    }
}

class Robot extends Thread {

    DepositoA depositoA;
    DepositoB depositoB;

    Semaphore full, empty;

    int id;
    static int ID = 0;

    Semaphore mySem,nextSem;

    Semaphore[] turni;
    static int ordine = 0;
    static Semaphore mutex = new Semaphore(1);

    LinkedList<Integer> myObject = new LinkedList<>();

    Semaphore mutexB = new Semaphore(1);
    Semaphore secondPhase = new Semaphore(0);

    public Robot(DepositoA depA, DepositoB depB, Semaphore full, Semaphore empty, Semaphore[] turni){
        this.id = ID;
        ID++;
        this.turni = turni;
        this.depositoA = depA;
        this.depositoB = depB;
        this.full = full;
        this.empty = empty;

        mySem = turni[id];
        nextSem = turni[(id+1)%2];


    }

    public void run(){

        try{
            mutex.acquire();
            mySem = turni[ordine];
            ordine++;
            nextSem = turni[(ordine)%2];
            System.out.println(getName()+" e sono arrivato "+ordine);
            if(ordine==2){
                turni[0].release();
            }
            mutex.release();
            
            while( myObject.size()<5){
                mySem.acquire();
                full.acquire();
                depositoA.preleva();
                System.out.println("Sono "+getName()+" ed estraggo un oggetto, rimangono "+ depositoA.depA.size()+ " oggetti nel depositoA");
                myObject.add(1);
                nextSem.release();
                empty.release();

                if(myObject.size()==5){
                    secondPhase.release(2);

                }
            }

            try{
                secondPhase.acquire();
                while(myObject.size() > 0){
                    mutexB.acquire();
                    depositoB.depB.add(1);
                    myObject.removeLast();
                    System.out.println("Sono "+getName() + " e ho scaricato un oggetto nel DepositoB, mi rimangono " + myObject.size()+ " oggetti");
                    mutexB.release();
    
                }
                System.out.println("Sono "+getName()+ " e ho finito gli oggetti da scaricare");
            } catch(Exception e){
                System.out.print(e);
            }

            

        } catch(Exception e){
            System.out.print(e);
        }
        
        
    }
}

class DepositoA {

    LinkedList<Integer> depA;

    public DepositoA(LinkedList<Integer> depA){
        this.depA = depA;
    }

    public void preleva(){
        depA.removeLast();
    }
}

class DepositoB {
    LinkedList<Integer> depB;

    public DepositoB(LinkedList<Integer> depB){
        this.depB = depB;
    }

    public void inserisci(){
        depB.add(1);
    }
}