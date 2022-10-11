import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.plaf.synth.SynthDesktopIconUI;;

public class App {
    public static void main(String[] args) throws Exception {

        final int n = 4;

        Semaphore[] turniA = new Semaphore[n];
        Semaphore[] turniB = new Semaphore[n];
        Semaphore mutexGiudice = new Semaphore(0);

        for ( int i  = 0; i < n; i++){
            turniA[i] = new Semaphore(0);
            turniB[i] = new Semaphore(0);
        }
        Giudice giudice = new Giudice(turniA, turniB, mutexGiudice);
        giudice.setName("[Giudice]");
        giudice.start();
        TeamA teamA[] = new TeamA[n];
        TeamB teamB[] = new TeamB[n];
        for ( int i = 0; i < n; i++){
            teamA[i] = new TeamA(turniA,mutexGiudice,giudice);
            teamB[i] = new TeamB(turniB,mutexGiudice,giudice);
            teamA[i].setName("[TeamA-"+i+"]");
            teamB[i].setName("[TeamB-"+i+"]");
            teamA[i].start();
            teamB[i].start();
        }
    }
}

class TeamA extends Thread {

    Semaphore[] turniA;
    int id;
    static int ID = 0;
    Semaphore mutexGiudice;
    final int n = 4;
    Giudice giudice;


    public TeamA(Semaphore[] turniA,Semaphore mutexGiudice,Giudice giudice){
        this.turniA = turniA;
        this.id = ID;
        ID++;
        this.giudice = giudice;
        this.mutexGiudice = mutexGiudice;

        if ( ID == n){
            mutexGiudice.release();
        }

    }

    public void run(){

        try{

            turniA[id].acquire();
            System.out.println(getName() + " ha corso 100 metri e passa il testimone");
            turniA[(id+1)%n].release();

        } catch( Exception e){

        }
        if ( this.id == n-1){
            System.out.println("TeamA ha finito!");
            if ( giudice.winner == ""){
                giudice.winner = "TeamA";
                giudice.mutexEnd.release();
            } else {
                giudice.mutexEnd.release();
            }

        }
    }
}

class TeamB extends Thread{

    Semaphore[] turniB;
    int id;
    static int ID = 0;
    Semaphore mutexGiudice;
    final int n = 4;
    Giudice giudice; //test proviamo a stampare chi ha vinto

    public TeamB (Semaphore[] turniB,Semaphore mutexGiudice, Giudice giudice){
        this.turniB = turniB;
        this.id = ID;
        ID++;
        this.giudice = giudice;
        this.mutexGiudice = mutexGiudice;

        if ( ID == n){
            mutexGiudice.release();
        }
    }

    public void run(){
        try{

            turniB[id].acquire();
            System.out.println(getName() + " ha corso 100 metri e passa il testimone");
            turniB[(id+1)%n].release();

        } catch( Exception e){

        }

        if ( this.id == n-1){
            System.out.println("TeamB ha finito!");
            if ( giudice.winner == ""){
                giudice.winner = "TeamB";
                giudice.mutexEnd.release();
            } else {
                giudice.mutexEnd.release();
            }

        }
    }

}

class Giudice extends Thread {

    Semaphore mutexGiudice;
    Semaphore[] turniA;
    Semaphore[] turniB;

    Semaphore mutexEnd = new Semaphore(0);
    String winner = "";

    public Giudice(Semaphore[] turniA, Semaphore[] turniB, Semaphore mutexGiudice){
        this.mutexGiudice = mutexGiudice;
        this.turniA = turniA;
        this.turniB = turniB;
    }

    public void run(){
        try{

            mutexGiudice.acquire(2);
            System.out.println(getName() + " da' il via alla gara!!");
            turniA[0].release();
            turniB[0].release();
            mutexGiudice.release(2);

            mutexEnd.acquire(2);
            System.out.println(getName() + " annuncia che ha vinto "  + winner);
            mutexEnd.release(2);
        } catch( Exception e){

        }
    }
}