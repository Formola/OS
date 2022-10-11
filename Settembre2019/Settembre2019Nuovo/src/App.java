import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        Deposito deposito = new Deposito();

        Semaphore mutexA = new Semaphore(1);
        Semaphore mutexB = new Semaphore(0);

        RobotA robotA1 = new RobotA(deposito,mutexB,mutexA);
        RobotA robotA2 = new RobotA(deposito,mutexB,mutexA);
        RobotB robotB = new RobotB(deposito,mutexB,mutexA);
        robotA1.setName("[RobotA1]");
        robotA2.setName("[RobotA2]");
        robotB.setName("[RobotB]");
        robotA1.start();
        robotA2.start();
        robotB.start();

    }
}

class RobotA extends Thread {

    Deposito deposito;
    Semaphore mutexB;
    Semaphore mutexA;
    
    static int inserimenti = 0;
    int id;
    static int ID = 0;


    public RobotA(Deposito deposito,Semaphore mutexB, Semaphore mutexA){
        this.deposito = deposito;
        this.mutexB = mutexB;
        this.mutexA = mutexA;
        this.id = ID;
        ID++;
    }

    public void run(){
        try{
            while(deposito.D1.size() > 0 ){

                mutexA.acquire();
                deposito.D1.removeLast();
                deposito.mutexD2.acquire();
                deposito.D2.add(1);
                System.out.println(getName() + " estrae un oggetto da D1 e lo inserisce in D2");
                deposito.mutexD2.release();

                inserimenti++;
                if ( inserimenti == 2){
                    inserimenti = 0;
                    mutexB.release();
                } else {
                    mutexA.release();
                }

                if ( deposito.D1.size() == 0 ) {
                    System.out.println("Oggetti finiti in D1");

                }
            } 
            
        
        } catch(Exception e){

        }

    }
}

class RobotB extends Thread {

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

            while ( deposito.D1.size() > 0 ){


                mutexB.acquire();
                deposito.mutexD1.acquire();
                deposito.D1.add(1);
                System.out.println(getName() + " inserisce un oggetto in D1");
                deposito.mutexD1.release();
                mutexA.release();

                if ( deposito.D1.size() == 1 ){
                    System.out.println(getName() + " ultimo oggetto inserito");
                    mutexB.release();
                }

            }

        } catch( Exception e){
            
        }

    }
}

class Deposito {

    LinkedList<Integer> D1 = new LinkedList<>();
    LinkedList<Integer> D2 = new LinkedList<>();

    Semaphore mutexD1 = new Semaphore(1);
    Semaphore mutexD2 = new Semaphore(1);

    public Deposito(){
        for ( int i = 0; i < 5; i++){
            D1.add(1);
        }
    }
}
