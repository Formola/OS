import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        final int k = 3;
        final int m = 5;
        Ram ram = new Ram();

        Semaphore canWrite = new Semaphore(1);
        Semaphore canRead = new Semaphore(0);
        Scrittore scrittori[] = new Scrittore[k];
        Lettore lettori[] = new Lettore[m];
        
        for ( int i = 0; i < k; i++){
            scrittori[i] = new Scrittore(ram, canWrite, canRead);
            scrittori[i].setName("[Scrittore"+i+"]");
            scrittori[i].start();
        }

        for ( int i = 0; i < m; i++){
            lettori[i] = new Lettore(ram, canWrite, canRead);
            lettori[i].setName("[Lettore"+i+"]");
            lettori[i].start();
        }

    }
}

class Scrittore extends Thread{

    Ram ram;
    Semaphore canRead;
    Semaphore canWrite;

    public Scrittore(Ram ram, Semaphore canWrite, Semaphore canRead){
        this.ram = ram;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public void run(){
        while(true){
            try{
                canWrite.acquire();
                System.out.println(getName() + " scrive in ram");
                ram.scrivi();
                canRead.release(3);

            } catch(Exception e){

            }
        }
    }

}

class Lettore extends Thread {

    Ram ram;
    Semaphore canRead;
    Semaphore canWrite;

    static Semaphore mutex = new Semaphore(1);
    static int i = 0;

    public Lettore(Ram ram,Semaphore canWrite, Semaphore canRead){
        this.ram = ram;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public void run(){

        while(true){
            try{
                canRead.acquire();
                System.out.println(getName() + " legge dalla ram");
                ram.leggi();

                mutex.acquire();
                i++;
                if ( i == 3){
                    canWrite.release();
                    i = 0;
                }
                mutex.release();
            } catch(Exception e){


            }
        }

    }

}

class Ram{
    LinkedList<Object> ram;

    Ram(){
        ram = new LinkedList<>();
    }

    public void scrivi(){
        ram.add(new Object());
    }

    public void leggi(){

    }
}