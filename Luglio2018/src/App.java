import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class App{
  
    public static void main ( String args []){
        Semaphore turni[] = new Semaphore [ Produttore.N];
        Deposito dep = new Deposito();
        Produttore prod [] = new Produttore[ Produttore.N];

        for( int i = 0 ; i < Produttore.N ; i++){
            turni[i] = new Semaphore(0);
            prod[i] = new Produttore(turni, dep);
            prod[i].start();
        }
    }
}

class Deposito {
    
   
    ArrayList<Object> deposito = new ArrayList<>();

    public void inserisci ( Object oggetto){
        deposito.add(oggetto);
    }
}

class Produttore extends Thread{
    Semaphore turni [] , mySem, nextSem; 
    Deposito dep; 
    int id , numOggetti=0; 

    static int ID = 0, N = 10, k=5;

    public Produttore (Semaphore turni[], Deposito d){
        dep = d;
        this.turni = turni;
        id= ID;
        ID++;
    }

    public void run (){

      try{

            mySem = turni[id];
            nextSem = turni [(id+1)%N];
            if( id == N -1 ) {
                turni[0].release();
            }

            for ( int j = 0 ; j<k ; j++){
                mySem.acquire();
                dep.inserisci(new Object()); 
                numOggetti++;
                System.out.println("Sono " + getName() + " con ID: " +id + " e sto inserendo l'oggetto " + numOggetti);
                if ( id == N-1) {
                    N--;
                    turni[N].release();

                } else {
                    nextSem.release();
                }
            }

        } catch (Exception e) {}

    }

}