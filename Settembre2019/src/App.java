import java.util.*;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {

        Deposito deposito = new Deposito();
        deposito.fillD1();

        Semaphore mutexD2 = new Semaphore(1);

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(0);



        RobotA robotA1= new RobotA(deposito, mutexD2,mutexB, mutexA);
        RobotA robotA2 = new RobotA(deposito, mutexD2, mutexB, mutexA);
        RobotB robotB = new RobotB(deposito, mutexB, mutexA);

        robotA1.setName("[RobotA-1]");
        robotA2.setName("[RobotA-2]");
        robotB.setName("[RobotB]");

        robotA1.start();
        robotA2.start();
        robotB.start();
    }
}

class RobotA extends Thread{

    Deposito deposito;
    Semaphore mutexD2;
    Semaphore mutexA;
    Semaphore mutexB;

    static int n_op = 0; //ci accedo con mutexA

    public RobotA(Deposito deposito,Semaphore mutexD2,Semaphore mutexB, Semaphore mutexA){
        this.deposito = deposito;
        this.mutexD2 = mutexD2;
        this.mutexB = mutexB;
        this.mutexA = mutexA;
    }

    public void run(){
        try{
            while ( deposito.deposito1.size()>0){
                mutexA.acquire();
                deposito.deposito1.removeLast();
                mutexD2.acquire();
                deposito.deposito2.add(1);
                mutexD2.release();
                n_op ++;
                System.out.println(getName() + " estrae un oggetto da D1 e lo inserisce in D2");

                if ( n_op==2){
                    n_op = 0;
                    mutexB.release();
                } else {
                    mutexA.release();
                }
                if ( deposito.deposito1.size()==0){
                    System.out.println("Oggetti finiti nel deposito D1");

                    break;
                }


                

                }

        } catch(Exception e){

        }

    }
}

class RobotB extends Thread{

    Deposito deposito;
    Semaphore mutexB;
    Semaphore mutexA;

    public RobotB(Deposito deposito,Semaphore mutexB, Semaphore mutexA){
        this.deposito = deposito;
        this.mutexB = mutexB;
        this.mutexA = mutexA;
    }

    public void run(){
        try{
            while( deposito.deposito1.size()>0){
                mutexB.acquire();
                System.out.println(getName() + " rimuove un oggetto da D2 e lo inserisce in D1");
                deposito.deposito2.removeLast();
                deposito.deposito1.add(1);
                if ( deposito.deposito1.size()==1){
                    mutexA.release();
                    mutexB.release();
                    break;
                } else {

                    mutexA.release();
                }
            }            
        } catch(Exception e){

        }

    }

}

class Deposito {

    LinkedList<Integer> deposito1 = new LinkedList<>();
    LinkedList<Integer> deposito2 = new LinkedList<>();

    public void fillD1(){
        for ( int i = 0 ; i < 5; i++){
            deposito1.add(1);
        }
    }


}