import java.util.*;
import java.util.concurrent.Semaphore;;

public class App {
    public static void main(String[] args) throws Exception {
        Libreria libreria = new Libreria();

        Semaphore mutexTizio = new Semaphore(1);
        Semaphore mutexCaio = new Semaphore(0);


        Tizio tizio = new Tizio(libreria, mutexTizio, mutexCaio);
        tizio.setName("[Tizio]");
        Caio caio = new Caio(libreria, mutexTizio, mutexCaio);
        caio.setName("[Caio]");

        tizio.start();
        caio.start();

    }
}

class Libreria {

    LinkedList<Integer> ScaffaleA = new LinkedList<>();
    LinkedList<Integer> ScaffaleB = new LinkedList<>();
}

class Tizio extends Thread {

    Libreria libreria;
    Semaphore mutexTizio;
    Semaphore mutexCaio;

    public Tizio(Libreria libreria, Semaphore mutexTizio, Semaphore mutexCaio) {
        this.libreria = libreria;
        this.mutexTizio = mutexTizio;
        this.mutexCaio = mutexCaio;
    }

    public void run() {
        while (libreria.ScaffaleA.size() < 9) {
            try {
                mutexTizio.acquire();
                libreria.ScaffaleA.add(1);
                System.out.println(getName() + " aggiunge 1 libro a scaffale A, ora ce ne sono " + libreria.ScaffaleA.size());
                mutexCaio.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (libreria.ScaffaleB.size() < 10) {
            try {
                mutexTizio.acquire();
                for (int i = 0; i < 5; i++) {
                    libreria.ScaffaleB.add(i);
                }
                System.out.println(
                        getName() + " aggiunge 5 libri a scaffale B, ora ce ne sono " + libreria.ScaffaleB.size());
                mutexTizio.release();
            } catch (Exception e) {

            }
        }

    }
}

class Caio extends Thread {

    Libreria libreria;
    Semaphore mutexTizio;
    Semaphore mutexCaio;


    public Caio(Libreria libreria, Semaphore mutexTizio, Semaphore mutexCaio) {
        this.libreria = libreria;

        this.mutexTizio = mutexTizio;
        this.mutexCaio = mutexCaio;

    }

    public void run() {
        while (libreria.ScaffaleA.size() < 10) {
            try {
                mutexCaio.acquire();
                libreria.ScaffaleA.add(1);
                System.out.println(
                        getName() + " aggiunge 1 libro a scaffale A, ora ce ne sono " + libreria.ScaffaleA.size());

                if (libreria.ScaffaleA.size() == 10) {

                    mutexCaio.release();
                    System.out.println("ScaffaleA riempito, passiamo allo scaffaleB");

                    //mutexTizio.release();
                } else {
                    mutexTizio.release();
                }
            } catch (Exception e) {

            }
        }

        while (libreria.ScaffaleA.size() == 10 && libreria.ScaffaleB.size() < 5) {
            try {
                mutexCaio.acquire();
                for (int i = 0; i < 5; i++) {
                    libreria.ScaffaleB.add(i);
                }
                System.out.println(
                        getName() + " aggiunge 5 libri a scaffale B, ora ce ne sono " + libreria.ScaffaleB.size());

                mutexTizio.release();
            } catch (Exception e) {

            }
        }
    }
}