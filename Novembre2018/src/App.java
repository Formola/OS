import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        LinkedList<Integer> coda = new LinkedList<>();

        ArrayList< Integer> monetina = new ArrayList<>();
        monetina.add(0,0);
        monetina.add(1,1);
        monetina.add(2,0);

        final int n = 3;
        Semaphore[] turni = new Semaphore[n];
        for ( int i = 0; i < n; i++){
            turni[i] = new Semaphore(0);
        }


        Produttore[] producers = new Produttore[n];
        for ( int i = 0; i < n; i++){
            producers[i] = new Produttore(coda,turni, monetina);
            producers[i].setName("[Produttore"+(i+1)+"]");
            producers[i].start();

        }
    }
}

class Produttore extends Thread{

    int id, inserimenti;
    static int ID = 0;
    static  int n= 3;

    Random random = new Random();

    LinkedList<Integer> coda;

    static int k = 4;
    Semaphore[] turni;

    static int IDriposo = -1;
    static int numeroOp = 0;
    static int stato[] = new int[n];

    ArrayList< Integer> monetina;

    


    public Produttore(LinkedList<Integer> coda, Semaphore[] turni, ArrayList< Integer> monetina){
        this.id = ID;
        ID++;
        this.coda = coda;
        this.turni = turni;
        inserimenti = 0;
        this.monetina = monetina;

        if ( ID == n){
            turni[0].release();
        }

    }

    public void run(){

        try{
            for ( int i = 0; i < k; i++){
                turni[id].acquire();

                if (monetina.get(id) == 1 ){
                    IDriposo = id;
                }  else {}

                if ( IDriposo == id){
                    System.out.println("Sono " + getName() + " e starò fermo questo giro");
                    if ( id == 2){
                        IDriposo = -1;
                        numeroOp = 0;
                        Collections.shuffle(monetina);
                    } else {}
                    turni[(id+1)%3].release();
                } else {
                    coda.add(1);
                    System.out.println(("Sono " + getName() + " e  ho inserito elemento numero: " +inserimenti));
                    numeroOp++;
                    inserimenti++;
                    if ( id == 2){
                        IDriposo = -1;
                        numeroOp = 0;
                        Collections.shuffle(monetina);
                    } else {}
                    turni[(id+1)%3].release();
                }
            }

            while(inserimenti < k){
                System.out.println("ultima fase, mancano da inserire " + (k-inserimenti) +" oggetti a testa");
                turni[id].acquire();
                if ( inserimenti < k){
                    coda.add(1);
                    System.out.println(("Sono " + getName() + " e  ho inserito elemento numero: " +inserimenti));
                    inserimenti++;
                    turni[(id+1)%3].release();
                } else {
                    turni[(id+1)%3].release();
                    break;
                    
                }
            }


        } catch(Exception e){

        }

    }
}