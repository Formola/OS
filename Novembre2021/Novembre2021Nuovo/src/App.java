import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int n = 60;
        Poste poste = new Poste();


        Utente[] utenti = new Utente[n];
        for ( int i = 0; i < n; i++){
            utenti[i] = new Utente(poste);
            utenti[i].setName("[Utente"+i+"]");
            utenti[i].start();
        }
    }
}

class Utente extends Thread{

    Poste poste;
    final int n = 60;
    int id;
    static int ID = 0;
    Random random = new Random();

    int op;
    String myOp = "";

    static Semaphore mutexA = new Semaphore(1);
    static Semaphore mutexB = new Semaphore(2);

    public Utente(Poste poste){
        this.poste = poste;
        this.id = ID;
        ID++;

        op = random.nextInt(2);
        if ( op == 0){
            myOp = "OpA";
        } else { myOp = "OpB";}
        

    }

    public void run(){

        try {

            poste.emptySala.acquire(); //entrano in sala di attesa, max 5
            //poste.mutexSala.acquire();
            System.out.println(getName()+ " e' entrato in sala di attesa");
            //poste.mutexSala.release();


            if ( myOp == "OpA"){ //coda sport A
                mutexA.acquire();
                poste.sportelloA.add(1);
                System.out.println(getName() + " viene servio allo SportelloA");
                mutexA.release();
                poste.emptySala.release();

            } else if ( myOp == "OpB"){ // coda sportB
                mutexB.acquire();
                poste.sportelloB.add(1);
                System.out.println(getName() + " viene servio allo SportelloB");
                mutexB.release();
                poste.emptySala.release();
            }




            



        } catch(Exception e){

        }

    }
}

class Poste {

    LinkedList<Integer> sala_di_attesa = new LinkedList<>();
    LinkedList<Integer> sportelloA = new LinkedList<>();
    LinkedList<Integer> sportelloB = new LinkedList<>();
    Semaphore emptySala = new Semaphore(5);
    Semaphore mutexSala = new Semaphore(1);
}