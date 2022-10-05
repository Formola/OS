import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        int n = 10;
        Utente utenti[] = new Utente[n];

        Semaphore emptyA = new Semaphore(20);
        Semaphore fullA = new Semaphore(0);

        Semaphore emptyB = new Semaphore(10);
        Semaphore fullB = new Semaphore(0);





        for ( int i  = 0; i < n ; i++){
            utenti[i] = new Utente(emptyA, fullA, emptyB, fullB);
            utenti[i].setName("[Utente"+i+"]");
            utenti[i].start();
        }
    }
}

class Utente extends Thread {

    Semaphore emptyA;
    Semaphore emptyB;
    Semaphore fullA;
    Semaphore fullB;

    int id;
    static int ID = 0;

    Random random = new Random();

    static Semaphore mutexSportello = new Semaphore(1);
    String nomeSportello;

    int num_sportello =  -1;

    public Utente(Semaphore emptyA, Semaphore fullA, Semaphore emptyB, Semaphore fullB){
        this.fullA = fullA;
        this.fullB = fullB;
        this.emptyA = emptyA;
        this.emptyB = emptyB;
        this.id = ID;
        ID++;
    }

    public void run(){

        try{
            emptyA.acquire();
            System.out.println(getName()+ " si trova in Sala1");
            emptyB.acquire();
            System.out.println(getName()+ " si trova in Sala2");
            
            num_sportello = random.nextInt(2);
            if ( num_sportello == 0){
                nomeSportello = "SportelloA";
            } else { nomeSportello = "SportelloB"; }

            mutexSportello.acquire();
            System.out.println(getName()+ " viene servito allo "+nomeSportello);
            mutexSportello.release();
            emptyB.release();
            emptyA.release();



        } catch(Exception e){

        }

    }
}

class Asl{

    LinkedList<Integer> Sala1 = new LinkedList<>();
    LinkedList<Integer> Sala2 = new LinkedList<>();
}