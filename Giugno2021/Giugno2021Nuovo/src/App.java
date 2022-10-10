import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {


        Deposito deposito = new Deposito();

        Semaphore mySem = new Semaphore(0);
        Semaphore nextSem = new Semaphore(0);

        Robot robot1 = new Robot(mySem, nextSem, deposito);
        robot1.setName("[Robot1]");
        Robot robot2 = new Robot(nextSem, mySem , deposito);
        robot2.setName("[Robot2]");
        robot1.start();
        robot2.start();
    }
}

class Robot extends Thread {

    LinkedList<Integer> myObject = new LinkedList<>();

    Semaphore mySem, nextSem;
    Deposito deposito;

    int id;
    static int ID = 0;

    public Robot(Semaphore mySem, Semaphore nextSem, Deposito deposito){
        this.nextSem = nextSem;
        this.mySem = mySem;
        this.deposito = deposito;
        this.id = ID;
        ID++;

        if ( ID == 2){
            if ( this.id == 0){
                mySem.release();
            } else {
                nextSem.release();
            }
        }

    }

    public void run(){

        try{

            while ( myObject.size() < 5){

                mySem.acquire();
                myObject.add(deposito.depositoA.removeLast());
                System.out.println(getName() + " ha estratto un oggetto dal DepositoA");
                nextSem.release();

            }
            if (this.id == 1){
                System.out.println("Terminati gli oggetti nel DepositoA, procediamo con l'inserimento in B");
            }

            while ( myObject.size()>0){
                deposito.mutexB.acquire();
                deposito.depositoB.add(myObject.removeLast());
                System.out.println(getName() + " ha inserito un oggetto nel DepositoB");
                deposito.mutexB.release();
            }



        } catch( Exception e){

        }

    }
}

class Deposito {

    LinkedList<Integer> depositoA = new LinkedList<>();
    LinkedList<Integer> depositoB = new LinkedList<>();

    Semaphore mutexB = new Semaphore(1);

    public Deposito(){
        for ( int i = 0; i < 10; i++){
            depositoA.add(1);
        }
    }


}