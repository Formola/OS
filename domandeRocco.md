## CLASSIC DI SISTEMI OPERATIVI

### SYSTEM CALL

- System call, esempio di esecuzione (schema con la trap) 

- Scrivere l'interfaccia della open 

- Cosa fa il SO quando un programma lo chiama per fare una fork(), in generale
  una system call penso intenda. Quali sono le strutture dati che
  vengono istanziate nel momento in cui faccio una fork? 

- Relativo a una system call fork, padre e figlio hanno una pcb identica o cambia qualche dato tra le due?

- Differenza chiamata tra una funzione normale generica e una system call

- come avviene il passaggio stato utente stato supervisore

- system call read? cosa a il S.O quando arriva una read?

- maschera dei segnali?? ( array di bit che rappresentano i segnali bloccati per un processo , supponiamo bit = 1 segnale blokkato...sidai ha senso)

- system call creat

- System call write, scrivere (interfaccia, che tipo sono i parametri, cosa fa etc)
  Dopo la scrittura cosa succede (inode, tabelle)

### GESTIONE PROCESSI

- PCB (process control block), cos'è? quali informazioni contiene?? devono essere residenti in memoria?? ( solo quelle x skeduling in memoria fisse...altre in user area)

- Stati di un processo (new, ready, running, ecc...)

- nascita di un processo, parla della shell e del processo init

- tabella dei processi? quali parametri trovo??

- quali parametri trovo nel pcb utili x schedulazione?? ( pid, ppid, userid, MASKERA..)

### SCHEDULING

- Algoritmo di schedulazione Linux

- algoritmo di schedulazione più usato?? come si calcola la priorita di tale algoritmo??

### THREAD - SEMAFORI - MONITOR

- cosa è la mutua esclusione

- Nel problema produttore-consumatore, cosa succede se invertiamo la posizione dei semafori di mutex e di spaziPieni e spaziVuoti? (illustrare il codice) 

- Problema di sincronizzazione dei lettori-scrittori (con codice) 

- Test&Set Lock, cos'è? Illustrare l'istruzione assembly e il funzionamento 

- Codice del semaforo? Perche syncronized?? Se due processi entrano nella regione critica senza il syncronized cosa succede? Il problema è l'up o il down?? Esempio sequenza di operazioni che violino la M.E

- Che cos'è un monitor? Cos'è una variabile di condizione di un monitor?  Quali sono i vincoli affinche una classe sia un monitor?? Perche non è perfettamente implementabile in java

- filosofi a cena!

- 

### MEMORIA

- Cosa succede quando si verifica un page fault?

- Descrivere l'algoritmo LRU col bit R; Che significa mettere 0 su tutta la colonna?

- Bit R e M?

- clock LRU second chance

- Descrivere la tabella delle pagine e il singolo record della tabella? 

- Cos'è il TLB? come funziona?? Nella memoria segmentata con paginazione cosa cambia nel TLB?? Che significa che è una memoria associativa?

- Traduzione indirizzo logico/fisico, segmentazione con paginazione. Che informazioni trovo sul record della tabella delle pagine??

- Cos'è il working set?? chi è w(k,t) e cosa rappresenta tale funzione? Come si è utile per creare una schedulazione migliroe

- algoritmo clock working set

- Data una memoria virtuale paginata (disegnata da lui) vuole sapere cosa sono l’indirizzo logico e quello fisico e come si passa dall’uno all’altro

- Parla del problemi di tempo e di spazio nella memoria paginata?? quali sono gli inconvenienti della segmentazione paginata??

- perche si introduce la segmentazine?? quali sono gli inconvenienti?? cme si passa da indirizzo logico a fisico

- memoria segmentata con paginazione?? perche si introduce e come passare da indirizzo logico a fisico??

- -Data la dimensione della memoria virtuale, della memoria fisica e della dimensione delle pagine calcolare il numero di pagine logiche nella tabella delle pagine e la dimensione di essa

- esercizio indirizzo logico/fisico in bit con dimensionamento tabella delle pagine, quante righe, dimensione della singola riga
  
  - è possibile in qualche modo ridurre la dimensione delle tab delle pagine?

### FILE SYSTEM

- Come è fatto l'i-node? Specificare come i primi N blocchi dell'i-node sono composti (e come quelli dopo N)? gli i node dove sono memorizzati sul disco?? quali informazioni contiene un i-node? 

- Com è fatta la directory DOS(dire i campi giusti)???

- Quali sono le strutture dati del FileSystem? Come è fatta una directory in UNIX? quanti campi ci sono e cosa c'è in ogni campo

- Cos'è la FAT table e come è fatta? cosa cambia sul filesystem basato sulla fat?

- Descrivere la tabella dei file aperti e il singolo record di questa tabella

- Cosa fa un S.O quando gli viene chiesto di aprire un file esistente, supponendo di utilizzare come riferimento Unix??

- Quando creo un file, come faccio ad assegnargli il numero di INODE?

- gestione dei blocchi liberi nel file system

- free list con disegno e bitmap

- Master file table

### RAID E I/O

- void speriamo sia veramente cosi

### UN PO DI CODICI... DA IMPARARE A MEMORIA??????

- Lettori e scrittori
  
  ```java
  import java.util.concurrent.Semaphore;
  
  public class lettoriScrittori {
  
      public static void main(String args[]) {
  
          Semaphore lettScritt = new Semaphore(1);
          Semaphore attesa = new Semaphore(1); // semaforo d'attesa per garantire la sincronizz.
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
  ```
  
  - Produttori e consumatori
    
    ```java
    import java.util.concurrent.Semaphore;
    
    public class produttoriConsumatori {
    
        public static void main(String args[]) {
    
            int nConsumatori = 5;
            int nProduttori = 2;
            Semaphore mutex = new Semaphore(1);
            Coda coda = new Coda(10); // dimensione massima coda;
    
            Consumatore c[] = new Consumatore[nConsumatori];
            Produttore p[] = new Produttore[nProduttori];
    
            for (int i = 0; i < nConsumatori; i++) {
                c[i] = new Consumatore(mutex, coda);
                c[i].start();
            }
    
            for (int i = 0; i < nProduttori; i++) {
                p[i] = new Produttore(mutex, coda);
                p[i].start();
            }
    
        }
    }
    
    class Coda {
    
        Semaphore full;
        Semaphore empty;
    
        public Coda(int n) {
            full = new Semaphore(0);
            empty = new Semaphore(n);
        }
    
        public void inserisci(Object o) {
    
        }
    
        public Object rimuovi() {
            return new Object();
        }
    }
    
    class Produttore extends Thread {
    
        Semaphore mutex;
        Coda coda;
        String nome;
        int id;
        static int ID;
    
        public Produttore(Semaphore mutex, Coda coda) {
            this.mutex = mutex;
            this.coda = coda;
            id = ID;
            ID++;
            nome = "Produttore" + (id + 1);
        }
    
        public void run() {
            try {
                while (true) {
                    coda.empty.acquire();
                    System.out.println("Sono " + nome + " e sto provando a produrre un oggetto");
                    mutex.acquire(); // accediamo alla struttura dati condivisa in mutex
                    Object o = new Object();
                    coda.inserisci(o);
                    System.out.println("Sono " + nome + " ed ho prodotto un oggetto");
                    mutex.release();
                    coda.full.release(); // coda riempipta
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    }
    
    class Consumatore extends Thread {
    
        Semaphore mutex;
        Coda coda;
        String nome;
        int id;
        static int ID;
    
        public Consumatore(Semaphore mutex, Coda coda) {
            this.mutex = mutex;
            this.coda = coda;
            id = ID;
            ID++;
            nome = "Consumatore" + (id + 1);
        }
    
        public void run() {
            try {
                while (true) {
                    coda.full.acquire();
                    System.out.println("Sono " + nome + " e sto provando a consumare un oggetto");
                    mutex.acquire();
                    coda.rimuovi();
                    System.out.println("Sono " + nome + " ed ho rimosso un oggetto");
                    mutex.release();
                    coda.empty.release();
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    }
    ```
  
  - FILOSOFI A CENA !!!
    
    ```c
    #define N 5
    #define LEFT (i + N - 1) % N
    #define RIGHT (i + 1) % N
    #define THIKING 0
    #define HUNGRY 1
    #define EATING 2
    
    typedef int semaphore;
    int state[N];        // array per tenere traccia dello stato dei filosofi
    semaphore mutex = 1; // mutex per le zone critiche
    semaphore s[N];      // semaforo di ciascun filosofo
    
    void philosopher(int i)
    {
        while (true)
        {
            think();
            take_forks(i);
            eat(); // yum-yum spaghetti :D
            put_forks(i);
        }
    }
    
    void take_forks(int i)
    {
        down(&mutex);
        state[i] = HUNGRY; // voglio mangiare quindi mi setto ad affamato
        test(i);           // prova ad acquisire le forchette
        up(&mutex);
        down(&s[i]); // ho finito di mangiare
    }
    
    void put_forks()
    {
        down(&mutex);
        state[i] = THIKING; // ho finito di mangiare
        test(LEFT);
        test(RIGHT); // verifichiamo se i filosofi accanto possano mangiare
        up(&mutex);  // può mangiare il prossimo filosofo
    }
    
    void test(int i)
    {
        if (state[i] == HUNGRY && state[LEFT] != EATING && state[RIGHT] != EATING)
        {
            state[i] = EATING; // se sono affamato e chi mi è affianco non sta mangiando posso mangiare io
            up(&s[i]);         // inizio a mangiare
        }
    }
    ```

- Semaforo e monitor rocco
  
  - semaforo
  
  ```java
  public class Semaphore {
    private int value, sospesi; // andiamo a segnare il valore max del semaforo e quanti sono in attesa
                              // se value è 1, semaforo binario classic!
  
  public Semaphore(int v) {
      value = v;
      sospesi = 0; // inizialmente non ci sono sospesi
  }
  
  public synchronized void down() {
  
      if (value == 0) {
          sospesi++;
          try {
              wait();
          } catch (Exception e) {
              e.printStackTrace();
          }
          sospesi--; // una volta usciti dal wait
      } else {
          value--; // abbasso ed entro nell'area critica
      }
  }
  
  public synchronized void up() {
      if (sospesi > 0) {
          notify(); // se c'è qualcuno in attesa lo svegliamo
      } else {
          value++; // altrimenti alzo il valore del semaforo e può accedere qualcun altro
      }
   }
  }
  ```
  
  - monitor
  
  ```java
  public class esempioMonitor {
  
      static final int N = 100;
      static producer p = new producer();
      static consumer c = new consumer();
      static our_monitor mon = new our_monitor();
  
      public static void main(String args[]) {
          p.start();
          c.start();
      }
  
      static class producer extends Thread {
          public void run() {
              int item;
              while (true) {
                  item = produce_item();
                  mon.insert(item);
              }
          }
  
          private int produce_item() {
              return 1;
          }
      }
  
      static class consumer extends Thread {
          public void run() {
              int item;
              while (true) {
                  item = mon.remove();
                  consume_item(item);
              }
          }
  
          private void consume_item(int tem) {
  
          }
      }
  
      static class our_monitor {
  
          private int buffer[] = new int[N]; // usiamo un buffer come variabile di condizione condivisa
          private int count = 0, lo = 0, hi = 0;
  
          public synchronized void insert(int val) {
              if (count == N)
                  go_to_sleep();
              buffer[hi] = val;
              hi = (hi + 1) % N;
              count++;
              if (count == 1)
                  notify();
          }
  
          public synchronized int remove() {
              int val;
              if (count == 0)
                  go_to_sleep();
              val = buffer[lo];
              lo = (lo + 1) % N;
              count--;
              if (count == N - 1)
                  notify(); // si è liberato un posto
              return val;
          }
  
          private void go_to_sleep() {
              try {
                  wait();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
  
  }
  ```

- Vabe ci metto qualche esempio system call x il meme spero ne chied
  
  ```c
  ###Padre e figlio Fork
  
  #include <stdio.h>
  #include <sys/types.h>
  #include <sys/wait.h>
  
  void main()
  {
  
      pid_t pid;
      int status;
  
      pid = fork(); // il primo figlio è generato
  
      if (!pid)
      { // diverso da pid, codice figlio.
  
          printf("Sono il primo figlio: PID = %d PPID = %d \n", getpid(), getppid());
          exit(0); // il processo si interrompe
      }
  
      else
      { // codice del padre
  
          pid = fork(); // genero il secondo figlio
  
          if (!pid)
          { // codice del secondo figlio
              printf("Sono il secondo figlio: PID = %d PPID = %d \n", getpid(), getppid());
              exit(0);
          }
  
          else
          { // codice del padre nuovamente
  
              pid = wait(&status); // attende che termini il primo figlio
              printf("Primo figlio terminato: PID = %d STATO = %d \n", pid, status);
  
              pid = wait(&status); // attende che termini il secondo figlio
              printf("Second figlio terminato: PID = %d STATO = %d \n", pid, status);
              printf("Programma terminato");
          }
      }
  }
  ```
  
  - shell
    
    ```c
    #include <stdio.h>
    #include <sys/types.h>
    #include <sys/wait.h>
    
    void main()
    {
    
        pid_t pid, procid;
        int status, k;
        size_t n;
    
        char buffer[80], prompt[30]; // abbiamo un buffer di caratteri
    
        sprintf(prompt, "myprompt:>"); // memorizziamo il risultato della stampa all’interno dell’array di caratteri
        write(1, prompt, 10);          // scriviamo sullo stoud myprompt:>
        bzero(buffer, 80);             // andiamo ad azzerare 80 bytes del buffer
        while (caratteri.letti != 0)   // leggo finchè la stringa non è vuota
        {
            // processiamo la linea di comando
            k = strlen(buffer);
            buffer[k - 1] = buffer[k] // rimuoviamo /n finale
                if (buffer == "esci") exit(0);
            printf("Attivazione processo figlio %s \n", buffer); // stampiamo il contenuto di buffer
            if ((pid = fork()) == 0)
            {
                // siamo nel nuovo processo
                attivazione processo con exec = > exec(buffer); // ci saranno poi eventuali altri argomenti
                if (problemi.di.attivazione)
                    exit(1);
            }
            procid = wait(&status);
            if (status != 0)
                write(2, "commmand not found\n", 14); // comando non trovato
            bzero(buffer, 80);                        // riazzeriamo
            write(1, prompt, 10);
        }
        exit(0); // finito
    }
    ```

- pipeline
  
  ```c
  #include <stdio.h>
  #include <stdlib.h>
  #include <sys/types.h>
  
  void main()
  { // esempio che sfrutta il reindirizzamento degli standard input ed output
    // per mettere in comunicazione due processi padre-figlio
    // senza dovere usare direttamente la pipe, grazie appunto all reindirizzamento
  
      int pid, status;
      int fd_pipe[2];
      bool pipeline = true;
  
      if ((pid = fork()) == 0)
      { // processo figlio
          if (pipeline)
          {
              pipe(fd_pipe); // creiamo la pipe e ora possiamo fare la fork così sarà condivisa
              if (fork() == 0)
              {                    // figlio, produttore
                  close(1);        // chiudiamo lo stdout del produttore
                  dup(fd_pipe[1]); // associamo alla pipe di scrittura lo stdout del produttore
                  close(fd_pipe[0]);
                  close(fd_pipe[1]);
                  exec(cmd1, ...); // quando il produttore andrà a scrivere sullo stdout, in realtà
                                   // andrà a farlo sulla pipe
              }
              else
              {                    // processo padre, consumatore
                  close(0);        // chiudiamo lo stdin del consumatore
                  dup(fd_pipe[0]); // associamo alla pipe di lettura lo stdin del consumatore
                  close(fd_pipe[1]);
                  close(fd_pipe[0]);
                  exec(cmd2, ...); // quando il consumatore andrà a leggere sullo stdin, in reltà
                                   // andrà a farlo sulla pipe
              }
          }
      }
  }
  ```

- padre figlio pipe
  
  ```c
  #include <stdio.h>
  #include <stdlib.h>
  
  void main()
  { // scriviamo e leggiamo 10 volte un intero nella pipe facendo
    // comunicare due processi padre-figlio
  
      int pid, j, c;
      int piped[2]; // l'array di file descriptor che passiamo alla pipe
  
      // apriamo la pipe creando due fd, uno per la lettura e l'altro per la scrittura e
      // li memorizziamo nell'array piped
  
      if (pipe(piped) < 0)
          exit(1); // pipe fallito
  
      if ((pid = fork()) < 0)
          exit(2);
      else if (pid == 0) // figlio, che ha una copia di piped[]
      {
          close(piped[1]); // chiudiamo per sicurezza il fd per la scrittura
          for (j = 1; j <= ; j++)
          {
              read(piped[0], &c, sizeof(int)); // leggiamo da piped[0] ed inseriamo in c
              printf("Figlio: ho letto dalla pipe il numero %d\n", c);
          }
          exit(0);
      }
      else
      {                   // padre
          close(piped[0]) // chiudiamo per sicurezza il fd per la lettura
              for (j = 1; j <= 10; j++)
          {
              write(piped[1], &j, sizeof(int))
                  printf("Padre: ho scritto nella pipe il numero %d\n", j);
              sleep(1);
          }
          exit(0); // una volta finito chiudo il processo
      }
  }
  ```

- peterson
  
  ```c
  #define FALSE 0
  #define TRUE 1
  #define N 2
  
  // implementazione della soluzione di Peterson di M.E, che prevede due metodi
  // enter_region(int i) e leave_region(int i) per regolamentare l'accesso alla zona critica
  
  int turn;
  int interested[N];
  
  void enter_region(int process) // passiamo il pid, 0 o 1
  {
  
      int other;
      other = 1 - process;        // il numero dell'altro processo
      interested[process] = true; // mostriamo di essere interessati
      turn = process;             // ci settiamo il turno
      while (turn == process && interested[other] == true)
      {
  
      } // attesa attiva, giro a vuoto nel while finchè l'altro non ha finito
  }
  
  void leave_region(int process)
  {
  
      interested[process] = false; // lasciamo la regione critica e quindi non siamo più interessati
  }
  ```
