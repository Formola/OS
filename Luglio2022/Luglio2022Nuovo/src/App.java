import java.util.*;
import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {

        Faldone faldone = new Faldone();

        Semaphore emptyCapo = new Semaphore(5);
        Semaphore mutexCapo = new Semaphore(0);

        Capufficio cap = new Capufficio(faldone,emptyCapo, mutexCapo);

        Impiegato imp1 = new Impiegato(faldone, emptyCapo, cap, mutexCapo);
        imp1.setName("[Impiegato1]");
        imp1.start();
        Impiegato imp2 = new Impiegato(faldone, emptyCapo, cap, mutexCapo);
        imp2.setName("[Impiegato2]");
        imp2.start();
        cap.setName("[Capufficio]");
        cap.start();

    }
}

class Impiegato extends Thread {

    Faldone faldone;
    int id;
    static int ID = 0;

    LinkedList<Integer> myPratiche;
    static Semaphore mutexPratiche = new Semaphore(0);

    Semaphore emptyCapo;
    Capufficio cap;
    Semaphore mutexCapo;

    public Impiegato(Faldone faldone, Semaphore emptyCapo, Capufficio cap, Semaphore mutexCapo) {
        this.faldone = faldone;
        this.id = ID;
        ID++;
        myPratiche = new LinkedList<>();
        this.emptyCapo = emptyCapo;
        this.cap = cap;
        this.mutexCapo = mutexCapo;

        if (ID == 2) {
            mutexPratiche.release();
        }
    }

    public void run() {

        try {
            while (faldone.pratiche.size() > 0) {

                mutexPratiche.acquire();
                faldone.pratiche.removeLast();
                myPratiche.add(1);
                System.out.println(getName() + " ha lavorato ad una pratica, ne rimangono " + faldone.pratiche.size());


                emptyCapo.acquire();
                cap.pratiche_da_firmare.add(1);
                mutexCapo.release();
                myPratiche.removeLast();
                System.out.println(getName() + " ripone una pratica sulla scrivania del capufficio");

                mutexPratiche.release();

            
            }
        } catch (Exception e) {

        }
        System.out.println(getName() + " : ha terminato");

    }
}

class Capufficio extends Thread {

    LinkedList<Integer> pratiche_da_firmare;
    Semaphore emptyCapo;
    Semaphore mutexCapo;
    Faldone faldone;

    public Capufficio(Faldone faldone,Semaphore emptyCapo, Semaphore mutexCapo) {
        pratiche_da_firmare = new LinkedList<>();
        this.emptyCapo = emptyCapo;
        this.mutexCapo = mutexCapo;
        this.faldone = faldone;
    }

    public void run() {
        try {

            while (pratiche_da_firmare.size() > 0 || faldone.pratiche.size()>0) {
                mutexCapo.acquire();

                    System.out.println(
                            getName() + " ci sono " + pratiche_da_firmare.size() + " da firmare sulla scrivania");
                    pratiche_da_firmare.removeLast();
                    System.out.println(getName() + " firma una pratica,ne rimangono " + pratiche_da_firmare.size());
                    emptyCapo.release();

                

            }

        } catch (Exception e) {

        }
    }
}

class Faldone {

    LinkedList<Integer> pratiche = new LinkedList<>();

    public Faldone() {
        for (int i = 0; i < 50; i++) {
            pratiche.add(i);
        }
    }

}