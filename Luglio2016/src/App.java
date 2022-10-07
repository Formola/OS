import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) {
        Semaphore semA = new Semaphore(1);
        Semaphore semB = new Semaphore(0);

        Semaphore semC = new Semaphore(1);
        Semaphore semD = new Semaphore(0);

        Coda codaX = new Coda();
        Coda codaY = new Coda();

        thread A = new thread(codaX, codaY, semA, semB);
        thread B = new thread(codaX, codaY, semB, semA);
        thread C = new thread(codaX, codaY, semC, semD);
        thread D = new thread(codaX, codaY, semD, semC);

        A.start();
        B.start();
        C.start();
        D.start();

    }
}

class thread extends Thread {
    static int K = 5;
    static int P = 8;

    Coda codaX, codaY ; 

    Semaphore mySem, nextSem;

    int id ; 
    static int ID =0;
    
    public thread (Coda codaX, Coda codaY , Semaphore mySem, Semaphore nextSem){
        this.mySem=mySem;
        this.nextSem=nextSem;
        this.codaX = codaX;
        this.codaY = codaY;
        id= ID;
        ID++;
    }
    @Override
    public void run() {
        try {
            if((id%4) == 0 || (id%4) == 1 ){
                for ( int i = 0 ; i<K; i++){
                    mySem.acquire();
                    System.out.println("Sono " + getName() + " e sto inserendo nella CODA X: " +i);
                    codaX.inserisci();
                    nextSem.release();
                }
                System.out.println("Inserimento CodaX terminato");
                codaX.full.release();

                codaY.full.acquire();
                System.out.println("Estrazione CodaY iniziato");
                for(int i = 0 ; i<P ;i++){
                    mySem.acquire();
                    System.out.println("Sono " + getName() + " e sto estraendo dalla CODA Y: " +i);
                    codaY.estrai();
                    nextSem.release();
                }
            }
            else if ((id%4)==2 || (id%4)==3){
                for (int i = 0; i < P; i++) {
                    mySem.acquire();
                    System.out.println("Sono " + getName() + " e sto inserendo nella CODA Y: " + i);
                    codaY.inserisci();
                    nextSem.release();
                }
                System.out.println("Inserimento CodaY terminato");
                codaY.full.release();
                codaX.full.acquire();
                System.out.println("Estrazione CodaX iniziato");
                for (int i = 0; i < K; i++) {
                    mySem.acquire();
                    System.out.println("Sono " + getName() + " e sto estraendo dalla CODA X: " +i);
                    codaX.estrai();
                    nextSem.release();
                }
            }
        } catch (Exception e) {}
    }
}

class Coda {
    ArrayList<Object> coda ;
    Semaphore full ;
    public Coda(){
        coda = new ArrayList<>();
        full = new Semaphore(0);
    }
    public void inserisci(){
        coda.add(new Object());
    }
    public Object estrai (){
        return coda.remove(0);
    }
}