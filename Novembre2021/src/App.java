import java.util.*;
import java.util.concurrent.Semaphore;;


public class App {
    public static void main(String[] args) throws Exception {
        
        final int N = 5;
        Utente users[] = new Utente[N];

        Semaphore emptyA = new Semaphore(5);
        Semaphore fullA = new Semaphore(0);

        Semaphore emptyB = new Semaphore(5);
        Semaphore fullB = new Semaphore(0);

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(2);

        LinkedList<Integer> attesaA = new LinkedList<>();
        LinkedList<Integer> attesaB = new LinkedList<>();

        SportelloA sportelloA = new SportelloA(emptyA,fullA,attesaA);
        SportelloB sportelloB = new SportelloB(emptyB,fullB,attesaB);

        for ( int i = 0 ; i < N ; i++){
            users[i] = new Utente(sportelloA,sportelloB,emptyA,emptyB, fullA,fullB,mutexA,mutexB);
            users[i].setName("[Utente"+i+"]");
            users[i].start();
        }

    }
}

class Utente extends Thread {

    Semaphore emptyA,emptyB,fullA,fullB;
    SportelloA sportelloA;
    SportelloB sportelloB;
    Random random = new Random();

    int opBanc;

    Semaphore mutexA, mutexB;

    public Utente(SportelloA sportelloA, SportelloB sportelloB, Semaphore emptyA, Semaphore emptyB, Semaphore fullA, Semaphore fullB, Semaphore mutexA, Semaphore mutexB){
        this.sportelloA = sportelloA;
        this.sportelloB = sportelloB;
        this.emptyA = emptyA;
        this.emptyB = emptyA;
        this.fullA = fullA;
        this.fullB = fullB;
        this.mutexA = mutexA;
        this.mutexB = mutexB;

        opBanc = random.nextInt(2);
    }
    

    public void run(){

        String operazione = "";
        if ( opBanc == 0 ){ operazione = "OpA"; } else { operazione = "OpB"; }

        try{
            System.out.println(getName() + " ha rollato "+operazione);
            if ( opBanc == 0) { //Sala A    
                emptyA.acquire();
                System.out.println(getName() + " si trova nella sala di attesa A");
                try{
                    mutexA.acquire();
                    System.out.println(getName()+ " viene servito allo sportello A");
                    mutexA.release();
                } catch(Exception e){
                    System.out.println(e);
                }
                fullA.release();
            } else { //Sala B

                emptyB.acquire();
                System.out.println(getName()+ " si trova nella sala di attesa B");
                try{
                    mutexB.acquire();
                    System.out.println(getName()+ " viene servito allo sportello B");
                    mutexB.release();
                }catch(Exception e){
                    System.out.println(e);
                }
                fullB.release();
            }

        } catch ( Exception e){
            System.out.println(e);
        }
    }
}

class SportelloA {

    Semaphore emptyA,fullA;

    LinkedList<Integer> attesaA = new LinkedList<>();

    public SportelloA(Semaphore emptyA,Semaphore fullA, LinkedList<Integer> attesaA){
        this.emptyA = emptyA;
        this.fullA = fullA;
        this.attesaA = attesaA;
    }   
}

class SportelloB {

    Semaphore emptyB,fullB;

    LinkedList<Integer> attesaB = new LinkedList<>();

    public SportelloB(Semaphore emptyB,Semaphore fullB, LinkedList<Integer> attesaA){
        this.emptyB = emptyB;
        this.fullB = fullB;
        this.attesaB = attesaB;
    }
}