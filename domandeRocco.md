## CLASSIC DI SISTEMI OPERATIVI



### GESTIONE PROCESSI

- PCB (process control block), cos'è?  quali informazioni contiene?? devono essere residenti in memoria?? 

- Stati di un processo (new, ready, running, ecc...) 

- nascita di un processo, parla della shell e del processo init

- tabella dei processi? quali parametri trovo??

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

- maschera dei segnali??

- system call creat

- System call write, scrivere (interfaccia, che tipo sono i parametri, cosa fa etc)
  Dopo la scrittura cosa succede (inode, tabelle)

### SCHEDULING

- Algoritmo di schedulazione Linux

- quali parametri trovo nel pcb utili x schedulazione??

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

-  Descrivere l'algoritmo LRU col bit R; Che significa mettere 0 su tutta la colonna?

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
