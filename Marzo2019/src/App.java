import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        LinkedList<Integer> coda = new LinkedList<>();

        final int n = 3;

        Semaphore[] turni = new Semaphore[n];
        for( int i = 0; i < n ; i++){
            turni[i] = new Semaphore(0);
        }

        Produttore[] producers= new Produttore[n];
        for ( int i = 0; i < n ; i++){
            producers[i] = new Produttore(coda,turni);
            producers[i].setName("[Produttore"+i+"]");
            producers[i].start();
        }

    }
}

class Produttore extends Thread {

    LinkedList<Integer> coda;

    final int n = 3;

    int id;
    static int ID = 0;

    Semaphore[] turni;

    Random random = new Random();
    int myOp ;

    static LinkedList<Integer> ops = new LinkedList<>();
    static Semaphore mutex = new Semaphore(1);
    static int maxOp = 1;

    public Produttore(LinkedList<Integer> coda, Semaphore[] turni){
        this.coda = coda;
        this.id = ID;
        ID++;

        this.turni = turni;
        myOp = random.nextInt(10)+1;
        ops.add(myOp);

        if ( ID == n){
            turni[0].release(); 

            for ( int i = 0 ; i < n; i++){
                if ( this.ops.get(i) > maxOp){
                    maxOp = this.ops.get(i);
                }
            }      
            maxOp = (maxOp*3)-1;
        }
        System.out.println(getName() + " deve inserire " + myOp+ " oggetti nella coda");

    }

    public void run(){
        try{
            
            while(maxOp > 0){

                if ( myOp > 0 ){

                    turni[id].acquire();
                    coda.add(1);
                    myOp--;
                    mutex.acquire();
                    maxOp--;
                    mutex.release();
                    System.out.println(getName() + " inserisce un oggetto nella coda, ne rimangono " + myOp);

                    turni[(id+1)%n].release();
                    

                } else if ( myOp == 0){
                    turni[id].acquire();
                    mutex.acquire();
                    if ( maxOp == 0){
                        maxOp--;
                        myOp = -1;
                        System.out.println("Tutti gli inserimenti sono terminati");
                        turni[0].release();
                        turni[1].release();
                        turni[2].release();
                    } else {
                        maxOp--;
                        System.out.println(getName()+ "  ha finito ma continua a garantire round robin x rispettare i turni");
                        turni[(id+1)%n].release();
                    }
                    mutex.release();
                }



            }
        } catch(Exception e){

        }
    }
}