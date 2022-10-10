import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {
        
        final int n = 4;
        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i <n; i++){
            turni[i] = new Semaphore(0);
        }




        thread thread[] = new thread[n];
        for ( int i = 0; i < n ; i++){
            thread[i] = new thread(turni);
            thread[i].setName("[thread"+i+"]");
            thread[i].start();
        }

    }
}

class thread extends Thread {

    Random random = new Random();
    static  int n =4;
    int k;
    int id;
    static int ID = 0;
    Semaphore[] turni;

    static int idmax = -1;
    static int kmax = 0;

    public thread(Semaphore[] turni){
        this.id = ID;
        ID++;
        k = random.nextInt(100);
        this.turni = turni; 

        if ( ID == n){
            turni[0].release();
        }
    }

    public void run(){
        try {
            turni[id].acquire();
            System.out.println(getName()+ ": id=" +id + " e k=" + k);

            if ( k > kmax){
                kmax = k;
                idmax = id;

            } else if ( k == kmax && id<idmax){
                idmax = id;
            }

            if ( id == n-1){
                System.out.println("Saranno eseguite : "+kmax+" operazioni, inizia il thread numero : " +idmax);
                turni[idmax].release();
            } else {
                turni[(id+1)%n].release();
            }
            
            for ( int i = 0; i < kmax; i++){
                turni[id].acquire();
                System.out.println(getName()+" : effettuo operazione numero : " + i);
                turni[(id+1)%n].release();
            }

        } catch(Exception e){

        }

    }
}