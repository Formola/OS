import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Deposito depA = new Deposito(10);
        Deposito depB = new Deposito(0);

        Semaphore mutexA = new Semaphore(1);

        Semaphore[] turni = new Semaphore[2];
        turni[0] = new Semaphore(0);
        turni[1] = new Semaphore(0);


        Robot robot1 = new Robot(depA,depB,mutexA,turni);
        robot1.setName("[Robot1]");
        Robot robot2 = new Robot(depA,depB,mutexA,turni);
        robot2.setName("[Robot2]");

        robot1.start();
        robot2.start();

    }


}


class Robot extends Thread {

    Deposito depA;
    Deposito depB;
    Semaphore mutexA;
    Semaphore[] turni;

    int id;
    static int ID=0;

    LinkedList<Integer> myObject = new LinkedList<>();

    public Robot(Deposito depA, Deposito depB, Semaphore mutexA, Semaphore[] turni){
        this.id = ID;
        ID++;
        this.depA = depA;
        this.depB = depB;
        this.mutexA = mutexA;
        this.turni = turni;

    }

    public void run(){
        try{
            while(depA.deposito.size()>0){

                if ( myObject.size()<5){
                    mutexA.acquire();
                    depA.deposito.removeLast();
                    myObject.add(1);
                    System.out.println(getName()+ " ha estratto un oggetto dal DepositoA");
                    mutexA.release();
                
                    if ( depA.deposito.size()==0){
                        System.out.println("Sono terminati gli oggetti nel DepositoA");
                        turni[0].release();

                    }
                }
            }

        } catch(Exception e){

        }

        try{
            while(myObject.size()>0){
                turni[id].acquire();
                depB.deposito.add(1);
                myObject.removeLast();
                System.out.println(getName()+ " ha inserito un oggetto dal DepositoB");
                turni[(id+1)%2].release();
    
                if ( myObject.size()==0){
                    System.out.println(getName()+ " ha terminato gli oggetti da inserire ");
                }
            }

            

        } catch(Exception e){

        }

        


    }
}

class Deposito {

    LinkedList<Integer> deposito = new LinkedList<>();
    public Deposito(int n){
        for ( int i = 0; i < n; i++){
            deposito.add(i);
        }
    }
}