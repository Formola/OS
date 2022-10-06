import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        CPU cpu = new CPU();
        final int n = 3;

        Semaphore turni[] = new Semaphore[n];
        turni[0] = new Semaphore(1);
        for ( int i = 1 ; i < n ; i++){
            turni[i] = new Semaphore(0);
        }

        Processo[] processi = new Processo[n];
        for( int i = 0; i<n; i++){
            processi[i] = new Processo(cpu, turni[i], turni[(i+1)%n]);
            processi[i].setName("[Processo"+i+"]");
            processi[i].start();
        }

    }
}

class Processo extends Thread {

    CPU cpu;

    final int n = 3;

    int id;
    static int ID = 0;

    int myOp;

    Random random = new Random();

    Semaphore mySem, nextSem;

    static int maxOp = 0;


    public Processo(CPU cpu, Semaphore mySem, Semaphore nextSem){
        this.cpu = cpu;
        this.id = ID;
        ID++;
        this.mySem = mySem;
        this.nextSem = nextSem;
        
        myOp = random.nextInt(10);
        if ( myOp > maxOp ){
            maxOp = myOp;
        }

    }

    public void run(){
        System.out.println(getName() + " e devo fare " + myOp + " operazioni");
        try{
            while(myOp>0){

                for ( int i = 0; i < maxOp; i++){
                    mySem.acquire();

                    if ( myOp<=0){
                        System.out.println(getName()+ " ha finito ma deve garantire round robin senza usare la cpu");
                    } else {
                        cpu.calcola();
                        sleep(50);
                        myOp--;
                        System.out.println(getName()+ " utilizza la cpu");
                    }
                    
    
                    if ( myOp == 0){
                        myOp = -1;
                        System.out.println(getName()+ " ha finito");
                        nextSem.release();
                    } else {
                        nextSem.release();
                    }
                }
            
            }
        } catch (Exception e){

        }

    }
}




class CPU{

    public void calcola(){
        
    }
}