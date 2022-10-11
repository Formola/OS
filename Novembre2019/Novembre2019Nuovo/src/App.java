import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        final int n = 4;

        LinkedList<Integer> factory = new LinkedList<>();

        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        P p[] = new P[n];
        for ( int i = 0 ; i < n; i++){
            p[i] = new P(factory,turni);
            p[i].setName("[P"+i+"]");
            p[i].start();
        }


    }
}

class P extends Thread {


    final int n = 4;
    LinkedList<Integer> factory;
    Random random = new Random();
    Semaphore[] turni;

    int id;
    static int ID = 0;
    static int next_start;
    static int op = 0;

    public P(LinkedList<Integer> factory, Semaphore[] turni){
        this.factory = factory;
        this.turni = turni;
        this.id = ID;
        ID++;

        if (ID == n ){
            turni[0].release();
        }
    }

    public void run(){

        try {

            while(true){
                turni[id].acquire();
                System.out.println(getName() + " esegue un job sulla fattoria");
                factory.add(1);
                op++;
                if ( op == n){
                    op = 0;
                    next_start = random.nextInt(n);
                    System.out.println(getName() + " era l'ultimo del turno precedente e ha deciso che il prossimo a inziare sara' P"+next_start);
                    turni[next_start].release();
                } else {
                    turni[(id+1)%n].release();
                }

            }


        } catch( Exception e){

        }

    }
}
