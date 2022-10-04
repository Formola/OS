import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Caveau caveau = new Caveau();

        Semaphore emptyA = new Semaphore(3);
        Semaphore emptyB = new Semaphore(2);
        Semaphore keyC = new Semaphore(1);
        Semaphore mutexCaveau = new Semaphore(0);

        P[] threadP = new P[10];
        for ( int i = 0; i < 10; i++){
            threadP[i] = new P(caveau,emptyA,emptyB,keyC, mutexCaveau);
            threadP[i].setName("[P"+i+"]");
            threadP[i].start();
        }
    }
}

class P extends Thread {

    Caveau caveau;

    int id ;
    static int ID = 0;

    LinkedList<Integer> myKeys = new LinkedList<>();
    Semaphore emptyA,emptyB, keyC, mutexCaveau;

    public P(Caveau caveau, Semaphore emptyA, Semaphore emptyB, Semaphore keyC, Semaphore mutexCaveau){
        this.caveau = caveau;
        this.id = ID;
        ID++;
        this.emptyA = emptyA;
        this.emptyB = emptyB;
        this.keyC = keyC;
        this.mutexCaveau = mutexCaveau;
    }

    public void run(){

        try{
            emptyA.acquire();
            System.out.println(getName()+" ha preso una chiave A");
            this.myKeys.add(1);
            caveau.chiaviA.removeLast();
                emptyB.acquire();
                System.out.println(getName()+" ha preso una chiave B");
                this.myKeys.add(1);
                caveau.chiaviB.removeLast();
                    keyC.acquire();
                    mutexCaveau.release();
                    caveau.chiaveC.removeLast();
                    System.out.println(getName()+" ha preso la chiave C");
                    mutexCaveau.acquire();
                    this.myKeys.add(1);
                    System.out.println(getName()+ " all'interno del caveau ha estratto il numero "+ caveau.estrai());

                    if ( myKeys.size() == 3){                        
                        myKeys.remove();
                        caveau.chiaveC.add(1);
                        myKeys.remove();
                        caveau.chiaviB.add(1);
                        myKeys.remove();
                        caveau.chiaviA.add(1);
                        emptyA.release();
                        emptyB.release();
                        keyC.release();
                    } 

                    if ( caveau.numeri.size()==0){
                        System.out.println("Rubato tutto dal caveau!!");
                    }
        }  catch(Exception e){

        }
    }
    
}

class Caveau{

    LinkedList<Integer> chiaviA = new LinkedList<>();
    LinkedList<Integer> chiaviB = new LinkedList<>();
    LinkedList<Integer> chiaveC = new LinkedList<>();

    int n = 11;


    ArrayList<Integer> numeri = new ArrayList<>();
    Random random = new Random();
    public Caveau(){
        for ( int i = 0; i < 10 ; i++){
            numeri.add(i);
        }

        for ( int i = 0; i < 3; i++){
            chiaviA.add(i);
        }
        for ( int i = 0; i < 2; i++){
            chiaviB.add(i);
        }
        chiaveC.add(1);
    }

    public int estrai(){
        n--;
        return (numeri.remove(random.nextInt(n)));
        
    }
}