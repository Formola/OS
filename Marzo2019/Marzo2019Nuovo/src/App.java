import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int n = 3;
        LinkedList<Integer> coda = new LinkedList<>();
        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }

        Produttore[] p = new Produttore[n];
        for ( int i = 0; i < n; i++){
            p[i] = new Produttore(coda,turni);
            p[i].setName("[Produttore"+i+"]");
            p[i].start();
        }

    }
}



class Produttore extends Thread {

    static int n = 3;
    LinkedList<Integer> coda;
    int id;
    static int ID = 0;

    Random random = new Random();
    int k;
    static int k_op[] = new int[n];

    static int max = -1;
    Semaphore[] turni;
    static int j = 0;
    static Semaphore mutex = new Semaphore(1);

    public Produttore(LinkedList<Integer> coda,Semaphore[] turni){
        this.coda = coda;
        this.k = random.nextInt(5);
        this.id = ID;
        ID++;
        k_op[id] = k;
        this.turni = turni;

        if ( ID == n){
            for ( int i = 0; i < n; i++){
                if ( max < k_op[i]){
                    max = k_op[i];
                }
            }
            turni[random.nextInt(n)].release();
        }
    }

    public void run(){

        try{
            System.out.println(getName()+ " ha prodotto " + k+ " oggetti");
            for ( int i = 1; i < max+1; i++){
                
                turni[id].acquire();
                mutex.acquire();
                if ( k > 0 ){
                    coda.add(1);
                    System.out.println(getName() + " ha inserito un elemento nella coda");
                    k--;
                    j++;
                    if ( j%(n) == 0){
                        System.out.println("Giro finito");
                        j = 0;
                        mutex.release();
                        turni[(random.nextInt(n))%n].release();

                    } else {
                        mutex.release();
                        turni[(id+1)%n].release();
                    }

                } else if ( k == 0){
                    System.out.println(getName() + " ha finito gli oggetti da inserire");
                    j++;
                    if ( j%(n) == 0){
                        System.out.println("Giro finito");
                        j = 0;
                        mutex.release();
                        turni[(random.nextInt(n))%n].release();
                    } else {
                        mutex.release();
                        turni[(id+1)%n].release();
                    }
                }
            }



        } catch( Exception e){

        }
    }
}