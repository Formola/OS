import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 2;
        Coda coda = new Coda();
        Deposito depA = new Deposito();
        Deposito depB = new Deposito();



        Robot robot[] = new Robot[n];
        for ( int i = 0; i < n; i++){
            robot[i] = new Robot(depA, depB, coda);
            robot[i].setName("[Robot"+i+"]");
            robot[i].start();
        }
    }
}


class Robot extends Thread {

    static int k = 40;
    int id;
    static int ID = 0;

    Deposito depA,depB;
    Coda coda;

    LinkedList<Integer> myObject = new LinkedList<>();

    static Semaphore mutex = new Semaphore(1);
    static int index_estratto = 0;

    public Robot(Deposito depA, Deposito depB,Coda coda){
        this.id = ID;
        ID++;
        this.depA = depA;
        this.depB = depB;
        this.coda = coda;
    }

    public void run(){

        try{

            for ( int i = 0; i < k; i++){

                mutex.acquire();
                myObject.add(coda.item.removeLast());
                index_estratto++;
                
                if ( index_estratto%2 == 0 ){
                    depB.mutexB.acquire();
                    depB.oggetti.add(myObject.removeLast());
                    System.out.println(getName() + " inserisce un oggetto nel depositoA");
                    depB.mutexB.release();
                } else {
                    depA.mutexA.acquire();
                    depA.oggetti.add(myObject.removeLast());
                    System.out.println(getName() + " inserisce un oggetto nel depositoB");

                    depA.mutexA.release();
                }
                mutex.release();

            }




            


        } catch(Exception e){

        }

    }
}

class Coda {

    LinkedList<Integer> item = new LinkedList<>();

    public Coda(){
        for ( int i = 0 ; i < 100; i++){
            item.add(1);
        }
    }


}

class Deposito {

    Semaphore mutexA = new Semaphore(1);
    Semaphore mutexB = new Semaphore(1);
    LinkedList<Integer> oggetti = new LinkedList<>();
}