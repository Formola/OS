import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {


        LinkedList<Integer> pratiche = new LinkedList<>();
        int k = 100;

        for ( int i = 0; i < k ; i++){
            pratiche.add(i);
        }
        
        LinkedList<Integer> praticheCap = new LinkedList<>();

        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(4); //conta pure lo zero
        
        Impiegato imp1 = new Impiegato(pratiche,praticheCap,full,empty);
        imp1.setName("[Impiegato-1]");
        Impiegato imp2 = new Impiegato(pratiche,praticheCap,full,empty);
        imp2.setName("[Impiegato-2]");
        Capufficio cap = new Capufficio(praticheCap, full,empty);
        cap.setName("[Capufficio]");

        imp1.start();
        imp2.start();
        cap.start();
    }
}

class Impiegato extends Thread{

    LinkedList<Integer> pratiche;
    LinkedList<Integer> praticheCap;
    int id;
    static int ID = 0;
    
    Semaphore full;
    Semaphore empty;

    static Semaphore mutexMyPratiche = new Semaphore(1);
    //static Semaphore mutexPraticheCap = new Semaphore(1);


    public Impiegato(LinkedList<Integer> pratiche, LinkedList<Integer> praticheCap, Semaphore full, Semaphore empty){
        this.id = ID;
        ID++;
        this.pratiche = pratiche;
        this.praticheCap = praticheCap;
        this.full = full;
        this.empty = empty;
    }

    public void run(){
        try{
            mutexMyPratiche.acquire();
            // /System.out.println(pratiche.size());

            while(pratiche.size() > 0 ) {
                pratiche.removeLast();
                System.out.println("Sono " +getName()+ " e ho lavorato a una pratica , ne rimangono " + pratiche.size());
    
                try {
                    empty.acquire();
                    //mutexPraticheCap.acquire();
                    praticheCap.add(1);
                } catch(Exception e){
                    System.out.println(e);
                }
                full.release();
                mutexMyPratiche.release();
                //mutexPraticheCap.release();
    
                if ( pratiche.size() == 0) {
                    mutexMyPratiche.release();
                    System.out.println("Sono "+getName()+" e comunico che sono terminate le pratiche!");
                }
            }

        } catch(Exception e){
            System.out.println(e);
        }




    }

}

class Capufficio extends Thread {

    LinkedList<Integer> praticheCap;
    int id;
    static int ID = 0;

    //static Semaphore mutex = new Semaphore(1);

    Semaphore full;
    Semaphore empty;

    public Capufficio(LinkedList<Integer> praticheCap, Semaphore full, Semaphore empty){
        this.praticheCap = praticheCap;
        id = ID;
        ID++;
        this.full = full;
        this.empty = empty;
    }

    public void run(){
        
        while(praticheCap.size()>=0){
            if ( praticheCap.size()==0){
                System.out.println("Sono il " +getName()+ " e ho 0 pratiche sulla mia scrivania");

            }
            try {
                //mutex.acquire();
                full.acquire();

                praticheCap.removeLast();
                System.out.println("Sono il " +getName()+ " e ho firmato una pratica, ora me ne rimangono " + praticheCap.size());

                empty.release();
                //mutex.release();
            } catch(Exception e){
                System.out.println("Non sono riuscito a firmare");
            }

        }

    }
}