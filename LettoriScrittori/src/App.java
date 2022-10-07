
import java.util.concurrent.Semaphore;

public class App {

    public static void main(String args[]) {

        Semaphore lettScritt = new Semaphore(1);
        Semaphore attesa = new Semaphore(1); // semaforo d'attesa
        Lettore l[] = new Lettore[3];

        Scrittore s1 = new Scrittore(lettScritt, attesa);
        Scrittore s2 = new Scrittore(lettScritt, attesa);

        for (int i = 0; i < l.length; i++) {
            l[i] = new Lettore(lettScritt, attesa);
            l[i].start();
        }
        s1.start();
        s2.start();
    }
}

class Lettore extends Thread {

    Semaphore lettscritt;
    Semaphore attesa;
    static int nLettori;
    static Semaphore mutex = new Semaphore(1);
    String nome;
    int id;
    static int ID;

    public Lettore(Semaphore lettscritt, Semaphore attesa) {
        this.lettscritt = lettscritt;
        this.attesa = attesa;
        id = ID;
        ID++;
        nome = "Lettore" + id;
    }

    public void run() {
        try {
            attesa.acquire();
            attesa.release(); // liberiamo lo scrittore così
            mutex.acquire();
            for (int i = 0; i < 5; i++) {
                System.out.println("Sono " + nome + " e sto avviando la lettura n " + (i + 1));
                nLettori++;
                if (nLettori == 1) {
                    lettscritt.acquire();
                }
                mutex.release(); // possiamo far entrare altri lettori intanto;
                System.out.println("Sono " + nome + " e sto effettuando la lettura n " + (i + 1));
                mutex.acquire();
                nLettori--; // ho finito di leggere
                if (nLettori == 0) {
                    lettscritt.release(); // se sono finiti i lettori libero lo scrittore
                }
                mutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



class Scrittore extends Thread {

    Semaphore lettscritt;
    Semaphore attesa;
    String nome;
    int id;
    static int ID;

    public Scrittore(Semaphore lettscritt, Semaphore attesa) {
        this.lettscritt = lettscritt;
        this.attesa = attesa;
        id = ID;
        ID++;
        nome = "Scrittore " + id;
    }

    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                attesa.acquire(); // blocco i lettori
                System.out.println("Sono " + nome + " e sto avviando la scrittura n " + (i + 1));
                lettscritt.acquire();
                System.out.println("Sono " + nome + " e sto effettuando la scrittura n " + (i + 1));
                lettscritt.release();
                attesa.release(); // finito
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}