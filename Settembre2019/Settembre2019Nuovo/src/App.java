import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        
        Deposito deposito = new Deposito();
        final int n = 2;
        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i< n; i++){
            turni[i] = new Semaphore(0);
        }
        Semaphore mutexB = new Semaphore(0);

        RobotA robotA1 = new RobotA(deposito,mutexB,turni);
        RobotA robotA2 = new RobotA(deposito,mutexB,turni);
        RobotB robotB = new RobotB(deposito,mutexB,turni);
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
    Semaphore[] turni;
    
    static int inserimenti = 0;
    int id;
    static int ID = 0;
    Random random = new Random();

    public RobotA(Deposito deposito,Semaphore mutexB, Semaphore[] turni){
        this.deposito = deposito;
        this.mutexB = mutexB;
        this.turni = turni;
        this.id = ID;
        ID++;

        if ( ID == 2){
            turni[random.nextInt(2)].release();
        }
    }

    public void run(){
        try{
            while(deposito.D1.size() > 0 ){

                turni[id].acquire();
                deposito.D1.removeLast();
                deposito.D2.add(1);
                System.out.println(getName() + " estrae un oggetto da D1 e lo inserisce in D2");

                inserimenti++;
                if ( inserimenti == 2){
                    inserimenti = 0;
                    mutexB.release();
                } else {
                    turni[(id+1)%2].release();
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
    Semaphore[] turni;
    Random random = new Random();

    public RobotB(Deposito deposito,Semaphore mutexB, Semaphore[] turni){
        this.deposito = deposito;
        this.mutexB = mutexB;
        this.turni = turni;
    }

    public void run(){

        try{

            while ( deposito.D1.size() > 0 ){


                mutexB.acquire();
                deposito.mutexD1.acquire();
                deposito.D1.add(1);
                System.out.println(getName() + " inserisce un oggetto in D1");
                deposito.mutexD1.release();
                turni[random.nextInt(2)].release();

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
