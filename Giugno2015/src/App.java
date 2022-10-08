import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {


        Fattoria fattoria = new Fattoria();
        final int n = 4;
        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i<n; i++){
            turni[i] = new Semaphore(0);
        }



        P[] prods = new P[n];
        for ( int i = 0; i < n; i++){
            prods[i] = new P(fattoria,turni);
            prods[i].setName("[P"+i+"]");
            prods[i].start();
        }

    }
}

class P extends Thread {

    Semaphore[] turni;
    Fattoria fattoria;

    final int n = 4;
    static int k ;
    int id;
    static int ID = 0;

    static Semaphore mutexToken = new Semaphore(0);
    static int id_owner_token = -1;
    LinkedList<Integer> mytoken;

    public P(Fattoria fattoria,Semaphore[] turni){
        this.id = ID;
        ID++;
        k = 2;
        this.turni = turni;
        this.fattoria = fattoria;
        mytoken = new LinkedList<>();

        if ( ID == 4){
            mutexToken.release();
            turni[0].release();
        }
    }

    public void run(){

        try{

            for ( int i =0 ; i < k ; i++){
                mutexToken.acquire();
                id_owner_token = this.id;
                mutexToken.release();
            
                turni[id].acquire();
                mytoken.add(1);
                if (mytoken.size()==1){
                    System.out.println(getName() + " ha acquisito il token");
                    mytoken.removeLast();
                    System.out.println(getName() + " passa il token al processo successivo");
                    System.out.println(getName() + "accede alla farm");
                    fattoria.accedi();
                    turni[(id+1)%n].release();
                } 
            }


            
        } catch( Exception e){

        }

    }
}

class Fattoria {
    LinkedList<Integer> farm = new LinkedList<>();

    public void accedi(){
        farm.add(1);
    }
}