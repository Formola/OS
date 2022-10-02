import java.util.concurrent.Semaphore;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        int n = 3;
        Random random = new Random();
        int k[] = new int[n];

        Semaphore turni[] = new Semaphore[n];

        for ( int i = 0; i < n ; i++){
            turni[i] = new Semaphore(0);
        }

        myThread threadlist[]= new myThread[n];

        for ( int i = 0; i < n; i++){
            k[i] = random.nextInt(100);
            System.out.println(k[i] + " - ");
        }

        for ( int i = 0; i<n ; i++){
            threadlist[i] = new myThread(i, k[i], turni);
            threadlist[i].start();
        }
    }
}

class myThread extends Thread {

    int id, k;
    Semaphore turni[];
    String threadName;

    static int idmax, kmax, N=3;

    public myThread(int id, int k , Semaphore[] turni){
        this.id = id;
        this.k = k;
        this.turni = turni;

        if ( id == N-1) {
            turni[0].release();  //la traccia dice che all inizio partono in ordine di id
        }
    }


    public void run(){

        try{
            turni[id].acquire();
            System.out.println("["+getName()+"] : id=" +id + " e k=" + k);
            
            if ( k > kmax) {
                kmax = k;
                idmax = id;
            } else if ( k == kmax && id < idmax) {
                idmax = id;
            }

            if ( id == N-1){
                System.out.println("Saranno eseguite : "+kmax+" operazioni, inizia il thread numero : " +idmax);
                turni[idmax].release();
            } else { turni[(id+1)%N].release();}

            for ( int i =0; i < kmax; i++){
                turni[id].acquire(); //alzo il semaforo , è il mio turno
                System.out.println("["+getName()+"] : effettuo operazione numero : " + i);
                turni[(id+1)%N].release(); //lascio il turno al prossimo
            }

        } catch( Exception e){
            System.out.println(e);
        }
    }
}