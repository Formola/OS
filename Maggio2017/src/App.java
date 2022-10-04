import java.util.*;
import java.util.concurrent.Semaphore;


public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("Signori benvenuti alle poste di casaluce");

        Semaphore salaAttesa = new Semaphore(10);
        Semaphore[] Sportelli = new Semaphore[3];
        for ( int i = 0 ; i < 3 ; i++){
            Sportelli[i] = new Semaphore(0);
        }

        final int n = 10;
        Utente utenti[] = new Utente[n];

        for ( int i = 0 ; i < n ; i++){
            utenti[i] = new Utente(Sportelli,salaAttesa);
            utenti[i].setName("[Utente"+i+"]");
            utenti[i].start();
        }

    }
}

class Utente extends Thread{

    static int n = 10;

    int id;
    static int ID = 0;

    Semaphore[] sportelli;
    Semaphore salaAttesa;
    String nomeSportello;

    public Utente(Semaphore[] sportelli, Semaphore salaAttesa){
        this.id = ID;
        ID++;
        this.sportelli = sportelli;
        this.salaAttesa = salaAttesa;

        if ( id == n-1){
            sportelli[0].release();
        }
    }

    public void run(){
        try{
            salaAttesa.acquire();
            System.out.println(getName() + " accede alla sala di attesa con id " + this.id);
            sportelli[id%3].acquire();
            if ( (id%3) == 0){
                nomeSportello = "Sportello A";
            } else if ( (id%3) == 1){
                nomeSportello = "Sportello B";
            } else { nomeSportello = "Sportello C"; }
            System.out.println(getName() + " con id "+this.id+ " completa un operazione allo : " + nomeSportello);
            sportelli[(id+1)%3].release();
            salaAttesa.release();
        } catch(Exception e){

        }
    }
}
