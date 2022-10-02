import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class App {

    public static void main(String args[]) {

        int numPlayers = 3;
        Semaphore turno[] = new Semaphore[numPlayers];
        Semaphore turno2[] = new Semaphore[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            turno[i] = new Semaphore(0);
            turno2[i] = new Semaphore(0);
        }
        Giocatore G0 = new Giocatore(turno, turno2);
        Giocatore G1 = new Giocatore(turno, turno2);
        Giocatore G2 = new Giocatore(turno, turno2);
        G0.start();
        G1.start();
        G2.start();
    }
}

class Mazzo {

    LinkedList<Integer> cards = new LinkedList<>();

    public void fill() {
        for (int i = 0; i < 40; i++) {
            cards.addFirst(i);
        }
    }

    public Mazzo() {
        fill();
    }
}

class Giocatore extends Thread {

    static Mazzo mazzo = new Mazzo();
    int myCards;
    static Semaphore mutex = new Semaphore(1);
    static Semaphore secondPhase = new Semaphore(0);
    Semaphore turno[];
    Semaphore turno2[];
    static int winnerCards;
    Semaphore endGame = new Semaphore(0);
    static int numPlayers = 3;
    int id;
    static boolean finito = true;
    static int ID = 0;
    static String winner;
    static int numOperations = 0;
    static int totCards;
    static int cont = 0;

    public Giocatore(Semaphore turno[], Semaphore turno2[]) {
        this.turno = turno;
        this.turno2 = turno2;
        id = ID;
        ID++;
        if (ID == 2) {
            turno[0].release();
        }
    }

    public void run() {
        try {
            while ((mazzo.cards.size() > 10 && myCards < 10 && totCards <= 30)) {
                turno[id].acquire();
                mazzo.cards.removeFirst();
                myCards++;
                totCards++;
                System.out.println("Sono " + getName() + " e ho " + myCards + " carte in mano");
                if (id == numPlayers - 1 && myCards == 10) {
                    secondPhase.release(); // al via la seconda fase
                } else {
                    turno[(id + 1) % numPlayers].release(); // rilascio in round Robin
                }
            }

            while (!(mazzo.cards.isEmpty())) {
                secondPhase.acquire();
                mutex.acquire();
                mazzo.cards.removeFirst();
                myCards++;
                if (winnerCards < myCards) {
                    winner = getName();
                    winnerCards = myCards;
                }
                System.out.println(
                        "Sono " + getName() + " e prendo una carta dal mazzo. Ora ho " + myCards
                                + " carte in mano");
                if (mazzo.cards.isEmpty()) {
                    mutex.release();
                    turno2[0].release();
                } else {
                    mutex.release();
                    secondPhase.release();
                }
            }

            turno2[0].acquire();
            System.out.println(
                    "Sono " + getName() + " ed il vincitore e' " + winner + " con " + winnerCards + " carte in mano");

        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

}