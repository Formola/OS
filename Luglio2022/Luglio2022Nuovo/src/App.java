import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {

        



        Impiegato imp1 = new Impiegato();
        imp1.setName("[Impiegato1]");
        imp1.start();
        Impiegato imp2 = new Impiegato();
        imp2.setName("[Impiegato2]");
        imp2.start();
        Capufficio cap = new Capufficio();
        cap.setName("[Capufficio]");
        cap.start();

    }
}

class Impiegato extends Thread{

    public Impiegato(){

    }

    public void run(){

    }
}

class Capufficio extends Thread {


    public Capufficio(){

    }

    public void run(){

    }
}