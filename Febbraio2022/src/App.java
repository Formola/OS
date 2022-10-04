import java.util.concurrent.Semaphore;

public class App{
    public static void main( String args[]){
        Semaphore semR1 = new Semaphore(0);
        Semaphore semR2 = new Semaphore(0);
        Semaphore finegara = new Semaphore(0);
        Giudice giudice = new Giudice(semR1,semR2, finegara);
        Runner R1 = new Runner(semR1, finegara, giudice);
        Runner R2 = new Runner(semR2, finegara, giudice);
        giudice.start();
        R1.start();
        R2.start();
    }

}

class Giudice extends Thread{
    Semaphore R1, R2 ; 
    Semaphore finegara;
    public String vincitore ;
    public Giudice ( Semaphore R1, Semaphore R2, Semaphore finegara){
        this.R1=R1;
        this.R2=R2;
        this.finegara=finegara;
    }
    public void run(){
        try {
            System.out.println("INIZIO DELLA GARA");
            R1.release();
            R2.release();

            finegara.acquire();
            System.out.println("Il thread vincitore e': " + vincitore);
        } catch (Exception e) {
        }
    }
}

class Runner extends Thread{
    Semaphore mySem, finegara;
    Giudice giudice ; static int fine = 0;
    static Semaphore mutex = new Semaphore(1);
    public Runner ( Semaphore mySem, Semaphore finegara, Giudice giudice){
        this.finegara=finegara;
        this.mySem=mySem;
        this.giudice=giudice;
    }

    public void run(){
        try{

        mySem.acquire();
        for(int i = 0 ; i<100 && fine == 0 ; i++){
        System.out.println("Sono " + getName() + " con i= " + i);
        if(i == 99) {
            fine=1;
            giudice.vincitore = getName();
            finegara.release();
        } else{}
        
        } 
   }catch(Exception e){}
}
}

