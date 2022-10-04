import java.util.*;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {
        
        Lotteria lotteria = new Lotteria();
        lotteria.fill();

        int[] turno = {2,0,3,1};
        Semaphore[] turni = new Semaphore[4];
        for ( int i = 0; i < 4; i++){
            turni[i] = new Semaphore(0);
        }

        P[] threads = new P[4];
        for ( int i = 0; i < 4; i++){
            threads[i] = new P(turno,turni, lotteria);
            threads[i].setName("[P"+i+"]");
            threads[i].start();
        }

    }
}

class P extends Thread{

    Random random = new Random();
    static int n = 4;
    
    int[] turno;

    int id;
    static int ID = 0;

    Lotteria lotteria;
    Semaphore[] turni;

    static Semaphore mutexTurni = new Semaphore(1);
    static Semaphore mutexLotteria = new Semaphore(1);

    Semaphore mySem, nextSem;
    int myturn,nextTurn;

    int estratto;

    static int cont = 0;


    public P(int[] turno, Semaphore[] turni , Lotteria lotteria){
        this.id = ID;
        ID++;
        this.turno = turno;
        this.lotteria = lotteria;
        this.turni = turni;

    }

    public void run(){

        

            try{
                mutexTurni.acquire();
                myturn = turno[id];
                cont++;
                System.out.println("Sono " + getName() + " di ID: " + id + " ed ho come turno " + myturn);
                if ( cont == n){
                    turni[0].release();
                } else {
                    mutexTurni.release();
                }

                while(true){
                turni[myturn].acquire();
                estratto = random.nextInt(90);
                System.out.println("Sono " + getName() + " di turno " + myturn + " ed estraggo: " + estratto);
                turni[(myturn+1)%n].release();
                }

            } catch(Exception e){
                //System.out.println(e);
            }
        

    }
}

class Lotteria {

    LinkedList<Integer> numeri_lotto = new LinkedList<>();

    public void fill(){
        for ( int i = 0; i<90; i++){
            numeri_lotto.add(i);
        }
    }
}
