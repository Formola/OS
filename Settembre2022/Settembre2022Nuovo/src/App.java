import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        Mazzo carte = new Mazzo();

        Semaphore turni[] = new Semaphore[3];
        for ( int i = 0; i < 3; i++){
            turni[i] = new Semaphore(0);
        }
        
        G giocatori[] = new G[3];
        for ( int i = 0 ; i < 3; i++){
            giocatori[i] = new G(carte,turni);
            giocatori[i].setName("[G"+i+"]");
            giocatori[i].start();
        }

    }
}


class G extends Thread {

    Mazzo mazzo;
    final int n =3;

    LinkedList<Integer> mycards;

    int id;
    static int ID = 0;
    static int k = 10;

    Semaphore[] turni;
    static Semaphore mutexFase2 = new Semaphore(0);

    static int[]  maxCards = new int[3];

    public G(Mazzo mazzo, Semaphore[] turni){
        this.mazzo = mazzo;
        mycards = new LinkedList<>();
        this.id = ID;
        ID++;
        this.turni = turni;

        if ( ID ==  3){
            turni[0].release();
        }

    }

    public void run(){

        try{

            for ( int i = 0; i < k; i++){
                turni[id].acquire();
                mazzo.mazzo.removeLast();
                mycards.add(1);
                System.out.println(getName() + " ha preso una carta dal mazzo");

                if ( this.id == 2 && mazzo.mazzo.size() == 10) {
                    System.out.println("Prima fase terminata, hanno tutti 10 carte");
                    mutexFase2.release(3);
                } else {    
                    turni[(id+1)%n].release();
                }
            }

            while( mazzo.mazzo.size()>0){
                mutexFase2.acquire();
                mazzo.mazzo.removeLast();
                mycards.add(1);
                System.out.println(getName() + " ha preso una carta dal mazzo , ora ne ha " + mycards.size());
                mutexFase2.release();
                if ( mazzo.mazzo.size()==0){
                    turni[0].release();
                }
            }

            maxCards[id] = mycards.size();
            System.out.println(getName() + " ha terminato con " +mycards.size() + " carte in mano");
            if ( this.id == 0 ){
                turni[id].acquire();

                int max = 0;
                int winnderId = 0;
                String winnerName  = "";
                for ( int i = 0; i < 3; i++){
                    if (  maxCards[i] > max){
                        max = maxCards[i];
                        winnderId = i;
                    }
                }
                if ( winnderId == 0 ){
                    winnerName = "G0";
                } else if ( winnderId == 1){
                    winnerName = "G1";
                } else { winnerName = "G2";}

                System.out.println(getName() +" proclama che il vincitore e' : "+winnerName);
                turni[id].release();
            }
            
        } catch( Exception e){

        }

    }
}

class Mazzo{
    LinkedList<Integer> mazzo = new LinkedList<>();

    public Mazzo(){
        for ( int i = 0; i < 40; i++){
            mazzo.add(i);
        }
    }
}