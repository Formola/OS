import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

//okkio, nella seconda fase un singolo thread una volta schedulato potrebbe compiere molte operazioni , poiche la traccia dice 40 carte di solito vince sempre uno con 20 carte in mano , ktm rocco

public class App {

    public static void main(String args[]){

        final int N = 3; //num giocatori

        LinkedList<Integer> mazzo = new LinkedList<>();

        for ( int i=0; i < 400; i++){
            mazzo.add(i);
        }

        Semaphore[] turni = new Semaphore[N];

        for ( int i = 0; i < N ; i++){
            turni[i] = new Semaphore(0);
        }

        Giocatore G0 = new Giocatore(mazzo,turni);
        Giocatore G1 = new Giocatore(mazzo,turni);
        Giocatore G2 = new Giocatore(mazzo,turni);

        G0.start();
        G1.start();
        G2.start();


    }
}

class Giocatore extends Thread {

    LinkedList<Integer> mazzo;
    Semaphore[] turni;

    int id;
    static int ID = 0;

    int mycards;

    static Semaphore secondPhase = new Semaphore(0);
    static Semaphore mutexcards = new Semaphore(1);
    static Semaphore thirdPhase = new Semaphore(1);

    static int N = 3;
    static int[] playersNumberOfCards = new int[N];

    Semaphore mySemaphore, nextSemaphore;

    public Giocatore(LinkedList<Integer> mazzo, Semaphore[] turni){
        this.mazzo = mazzo;
        this.turni = turni;
        id = ID;
        ID++;
        mycards = 0;
        mySemaphore = turni[id];
        nextSemaphore = turni[(id+1)%N];

        if ( id == 2) {
            turni[0].release();
        }
    }

    public void run (){

        while ( mycards < 10) {
            try{
                mySemaphore.acquire();
            } catch ( Exception e){
                System.out.println(e);
            }

            mazzo.removeLast();
            mycards++;
            System.out.println("["+getName()+"] ho pescato una carta dal mazzo - [" + mycards + " carte in mano e " + mazzo.size() +" carte nel mazzo rimanenti]");
            nextSemaphore.release();

            if ( mycards == 10 && id == 2) {
                secondPhase.release(3);
                System.out.println("Seconda fase");
            }
        }

        try{
            secondPhase.acquire();
        } catch(Exception e){
            System.out.println(e);
        }

        while( mazzo.size() > 0) {
            try{    
                mutexcards.acquire();
            } catch ( Exception e){
                System.out.println(e);
            }

            try{
                mazzo.removeLast();
                mycards++;
                System.out.println("["+getName()+"] ho pescato una carta dal mazzo - [" + mycards + " carte in mano e " + mazzo.size() +" carte nel mazzo rimanenti]");

            } catch (NoSuchElementException e) {
                //System.out.println("["+getName()+"]mazzo finito");
                System.out.println("Sono ["+getName()+"] e ho finito con "+mycards+" carte in mano");
            }
            mutexcards.release();
        }

        playersNumberOfCards[id] = mycards;

        if ( id == 0){
            try {
                thirdPhase.acquire();
            } catch ( Exception e){
                System.out.println(e);
            }

            int max = 0;
            int winnerID = 0;
            for ( int i = 0; i < N ; i++){
                if ( playersNumberOfCards[i] > max){
                    max = playersNumberOfCards[i];
                    winnerID = i;
                }
                
            }
            System.out.println("L'annunciatore ["+getName()+"] proclama che il Vincitore e' : Thread" + winnerID + " with " + max + " cards");
            thirdPhase.release();
        }
    }
}
