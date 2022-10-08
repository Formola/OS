import java.util.concurrent.Semaphore;

public class App {

    public static void main(String args[]) {

        Farm farm = new Farm();
        int kOperazioni = 3;
        int N = 5;
        Processo p[] = new Processo[N];
        Semaphore token = new Semaphore(1);

        for (int i = 0; i < N; i++) {
            p[i] = new Processo(token, kOperazioni, farm);
            p[i].start();
        }
    }
}

class Farm {

    public void work(String nome, int i) {
        System.out.println("Sono " + nome + " ed effettuo l'operazione n " + (i + 1) + " nella Farm");
    }
}

class Processo extends Thread {

    Farm farm;
    static Semaphore mutex = new Semaphore(1);
    Semaphore semToken;
    boolean token = false;
    int id;
    static int ID;
    String nome;
    int kOperazioni;

    public Processo(Semaphore semToken, int kOperazioni, Farm farm) {
        this.semToken = semToken;
        this.kOperazioni = kOperazioni;
        this.farm = farm;
        id = ID;
        ID++;
        nome = "P" + id;
    }

    public void run() {
        try {
            for (int i = 0; i < kOperazioni; i++) {
                semToken.acquire();
                System.out.println("Sono " + nome + " ed acquisisco il token");
                token = true;
                System.out.println("Sono " + nome + " ed ho depositato il token");
                token = false;
                semToken.release();

                mutex.acquire();
                farm.work(nome, i);
                mutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}