import java.util.*;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {
        final int k = 30;
        final int n = 3;

        Deposito deposito = new Deposito(k);
        deposito.fillDeposito();

        thread[] threads = new thread[n];
        Semaphore[] turni = new Semaphore[n];

        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        for(int i = 0; i < n ;i++){
            threads[i] = new thread(deposito,turni);
            threads[i].setName("[Thread"+i+"]");
            threads[i].start();
        }
    }
}

class Deposito {

    int k;
    LinkedList<Integer> gettoni = new LinkedList<>();

    public Deposito(int k ){
        this.k = k;
    }

    public void fillDeposito(){
        for ( int i = 0; i<k; i++){
            gettoni.add(i);
        }
    }
}

class thread extends Thread{

    Deposito deposito ;
    static int n = 3;

    int id;
    static int ID = 0;

    Random random = new Random();

    Semaphore[] turni;

    int next=0;

    public thread(Deposito deposito, Semaphore[] turni){
        this.deposito = deposito;
        this.turni = turni;
        this.id = ID;
        ID++;

        if ( ID == n-1){
            turni[0].release();
        }

    }

    public void run(){

        try{
            while(true){
                turni[id].acquire();
                deposito.gettoni.removeLast();
                System.out.println("Sono " +getName() + " e ho estratto un gettone, ne restano " + deposito.gettoni.size() );
                if ( deposito.gettoni.size()>n){
                    next = random.nextInt(n);
                    turni[next].release();
                } else if ( deposito.gettoni.size() == n){
                    System.out.println("Inizia il ROUND ROBIN - ULTIMO GIRO, Il primo a iniziare sara' Thread"+((id+1)%n));
                    turni[(id+1)%n].release();
                } else if ( deposito.gettoni.size()==0){
                    break;
                } else {
                    turni[(id+1)%n].release();
                }
            }


        } catch(Exception e){
            System.out.println(e);
        }
    }
}