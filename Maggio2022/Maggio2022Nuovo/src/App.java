import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n =3;

        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n ; i++){
            turni[i] = new Semaphore(0);
        }

        G giocatori[] = new G[3];
        for ( int i = 0; i < n; i++){
            giocatori[i] = new G(turni);
            giocatori[i].setName("[Giocatore"+i+"]");
            giocatori[i].start();
        }

    }
}

class G extends Thread {

    static int n = 3;
    int id;
    static int ID = 0, ordine = 0;

    static Semaphore mutexOrdine = new Semaphore(0);

    Semaphore[] turni;

    Random random = new Random();

    Semaphore mySem, nextSem;

    int mycards = 10;

    static int[] carteRimaste = new int[n];
    static int numGiocatori = 3;

    public G(Semaphore[] turni){
        this.id = ID;
        ID ++;
        this.turni = turni;


        if ( ID == 3){
            mutexOrdine.release();
        }

    }

    public void run(){

        try {
            
            mutexOrdine.acquire();
            System.out.println("Sono "+ getName() + " e sono arrivato " + ordine);
            mySem = turni[ordine];
            nextSem = turni[(ordine+1)%n];
            ordine++;
            if ( ordine == 3) {
                turni[0].release();
            }
            mutexOrdine.release();

            while ( numGiocatori > 1){
                mySem.acquire();
                
                if ( numGiocatori == 1 && mycards> 0){
                    System.out.println("Sono " +getName() + " e ho vinto con " + mycards+ " in mano");
                } else  {
                    if ( mycards > 0 ){
                        int removedCards = random.nextInt(2)+1;
                        if ( removedCards == 1 ){
                            mycards--;
                        } else {
                            mycards-=2;
                        }
                        System.out.println(getName() + " ha posato " + removedCards + " sul tavolo");

                        if ( mycards<=0){
                            System.out.println("Sono " + getName() + " e ho finito tutte le carte");
                            numGiocatori--;
                        }
                    }
                }
                nextSem.release();
            }
        } catch( Exception e){

        }
    }

}