import java.security.DrbgParameters.NextBytes;
import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Fattoria fattoria = new Fattoria();


        final int n = 5;

        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        P produttori[] = new P[n];
        for ( int i = 0; i< n; i++){
            produttori[i] = new P(fattoria,turni);
            produttori[i].setName("[P"+i+"]");
            produttori[i].start();
        }
    }
}

class P extends Thread{

    Fattoria factory;
    Semaphore[] turni ;

    int id ;
    static int ID = 0;
    Random random = new Random();

    final int n = 5;

    static int n_op = 0;

    static int randomId = 0;

    public P(Fattoria factory,Semaphore[] turni ){
        this.factory = factory;
        this.turni = turni;
        this.id = ID;
        ID++;

        if ( ID == n){
            turni[0].release();
        }
    }

    public void run(){
        try{
            turni[id].acquire();
            System.out.println(getName() + " esegue un work in fattoria");

            if ( id == n-1){
                n_op = 0;
                randomId = random.nextInt(n);
                System.out.println("Primo giro finito , il successivo primo a iniziare sarà P"+randomId);
                turni[randomId].release();
            } else { turni[(id+1)%n].release();}

            while(n_op >=0 ) {
                turni[id].acquire();

                if ( n_op < n ){
                    System.out.println(getName() + " esegue un work in fattoria");
                    n_op++;
                    turni[(id+1)%n].release();
                } else if ( n_op == n ){
                    n_op = 0;
                    randomId = random.nextInt(n);
                    System.out.println("Giro finito , il successivo primo a iniziare sarà P"+randomId);
                    turni[randomId].release();

                }


            }

        } catch(Exception e){

        }
    }
}

class Fattoria {

    LinkedList<Integer> factory = new LinkedList<>();
    public void work(){

    }
}