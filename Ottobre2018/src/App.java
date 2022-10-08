import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        final int k = 10;
        Scaffale scaffale = new Scaffale(k);

        Impiegato impiegato1 = new Impiegato(scaffale);
        Impiegato impiegato2 = new Impiegato(scaffale);
        impiegato1.setName("[Impiegato1]");
        impiegato2.setName("[Impiegato2]");
        Dirigente dirigente = new Dirigente(scaffale);
        dirigente.setName("[Dirigente]");
        impiegato1.start();
        impiegato2.start();
        dirigente.start();
    }
}

class Impiegato extends Thread {

    Scaffale scaffale;


    public Impiegato(Scaffale scaffale){
        this.scaffale = scaffale;
    }

    public void run(){
        while(scaffale.pratiche.size()>0){
            try {
                scaffale.mutexPratiche.acquire();
                Object pratica = scaffale.pratiche.removeLast();

                System.out.println(getName() + " ha lavorato una pratica");

                scaffale.praticheLavorate.add(pratica);
                scaffale.mutexPraticheLavorate.release(); //svegliamo il boss!
    
                scaffale.mutexPratiche.release();
            } catch( Exception e){
            }
        }

    }
}

class Dirigente extends Thread {

    Scaffale scaffale;


    public Dirigente(Scaffale scaffale){
        this.scaffale = scaffale;

    }

    public void run(){

        try{
            scaffale.mutexPraticheLavorate.acquire();
            while(scaffale.praticheLavorate.size()>0){

                Object pratica = scaffale.praticheLavorate.removeLast();

                if ( scaffale.praticheLavorate.size() >= 0){
                    System.out.println(getName()+ " firma una pratica");
                    scaffale.praticheFirmate.add(pratica);
                    scaffale.mutexPraticheLavorate.release();
                }
                
                if ( scaffale.praticheFirmate.size() == scaffale.k){
                    System.out.println("Il dirigente ha firmato le " + scaffale.k + " pratiche, finito!!");
                }

            }
            
        } catch(Exception e){

        }


    }
}

class Scaffale {

    Semaphore mutexPratiche;
    Semaphore mutexPraticheLavorate;

    int k;

    LinkedList<Object> pratiche;
    LinkedList<Object> praticheLavorate;
    LinkedList<Object> praticheFirmate;

    Scaffale(int k){
        this.k = k;
        mutexPratiche = new Semaphore(1);
        mutexPraticheLavorate = new Semaphore(0);
        pratiche = new LinkedList<>();
        for ( int i = 0; i < k; i++){
            pratiche.add(new Object());
        }
        praticheLavorate = new LinkedList<>();
        praticheFirmate = new LinkedList<>();
        
    }
}