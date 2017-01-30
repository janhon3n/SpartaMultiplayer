package Client;

import Game.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Miquel on 19.1.2017.
 */
public class OnlineTaistelu {

    private SpartaClient ownClient;
    private Scanner sc;
    private ClientServerInterface opponentClientServer;
    private boolean myTurn;
    private Pelaaja pelaaja;
    private boolean opponentHasIntroduced = false;
    private boolean iHaveIntroduced = false;
    private Hahmo opponentHahmo;
    private boolean crithit = false;
    private boolean hits = false;

    private Valikko taisteluValikko;

    public OnlineTaistelu(SpartaClient ownClient, ClientServerInterface opponentClientServer, boolean oneWhoStarts, Pelaaja pelaaja, Scanner sc){
        this.ownClient = ownClient;
        this.sc = sc;
        this.pelaaja = pelaaja;
        this.myTurn = oneWhoStarts;
        this.opponentClientServer = opponentClientServer;
        taisteluValikko = new Valikko(sc);
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("Löy vastustajaa") {
            @Override
            public void suorita() {
                attackOpponent();
            }
        });
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("Tilaa sparta") {
            @Override
            public void suorita() {

            }
        });
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("emt") {
            @Override
            public void suorita() {

            }
        });
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("Ota vähän lepiä") {
            @Override
            public void suorita() {

            }
        });
    }

    public void startIntroduce() throws RemoteException {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    simulateEnterPress();
                }
            }, 10000);
            System.out.print("Lähetä terveiset vastustajalle: ");
            sc.nextLine();
            String msg = sc.nextLine();
            iHaveIntroduced = true;
            opponentClientServer.introduce(pelaaja.annaNimi(), pelaaja.annaTaso(), msg);
            opponentClientServer.updateStats(this.pelaaja);
    }

    public void executeTurn() throws RemoteException {
        System.out.println("My turn!");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(pelaaja.elossa()) {
            //ask user input
            taisteluValikko.tulosta();
            taisteluValikko.pakotaValinta();
        } else {
            die();
        }
        this.myTurn = false;
        System.out.println("Opponents turn.");
    }

    public boolean checkTurn(){
        return myTurn;
    }
    public void setTurn(boolean turn){
        this.myTurn = turn;
    }
    public void opponentIntroduced(){
        this.opponentHasIntroduced = true;
    }
    public boolean opponentHasIntroduced(){
        return opponentHasIntroduced;
    }
    public boolean iHaveIntroduced(){
        return iHaveIntroduced;
    }

    public void simulateEnterPress(){
        if(iHaveIntroduced == false){
            try {
                Robot robot = new Robot();

                // Simulate a key press
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateStats(Hahmo hahmo){
        this.opponentHahmo = hahmo;
    }

    /* --------------- */
    public void attackOpponent() {
        String msg;
        double damage;
        boolean crithit;
        if (!(Math.random() * 100 < this.pelaaja.annaTarkkuus())) {
            hits = false;
            msg = (this.pelaaja.annaNimi() + " osui harhaan");
            damage = 0;
            crithit = false;

        } else {

            hits = true;
            if (Math.random() < this.pelaaja.annaCritRate()) { //jos critical hit
                crithit = true;
            } else {
                crithit = false;
            }

            damage = ((this.pelaaja.annaVoima()) / (((0.2 + (Math.random()* 0.8)) * this.opponentHahmo.annaSuoja())));


            if (crithit) {
                msg = (this.pelaaja.annaNimi() + " iskee viholliseen " + this.opponentHahmo.annaNimi() + " kriittistä vahinkoa: " + (int) damage + "!");
                damage = (damage) + (Math.random() * (1.5 * damage));
            } else {
                msg = (this.pelaaja.annaNimi() + " iskee vihollista " + this.opponentHahmo.annaNimi() + " aiheuttaen vahinkoa: " + (int) damage);
            }
        }



        System.out.println(msg);
        SpartaAction sa = new SpartaAction((int) damage, crithit, msg);
        for(int i = 0; i < 5; i++) {
            try {
                opponentClientServer.makeAnAction(sa);
                return;
            } catch (RemoteException e) {
                System.out.println("Yhteydessä ongelma. Yritetään uudestaan...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
        System.out.println("Yhteys menetetty :(");
        ownClient.closeGame();

    }

    public void die() throws RemoteException {

        String msg = pelaaja.annaNimi() + " kaatuu maahan tuskissaan.";
        System.out.println(msg);
        SpartaAction spartaAction = new SpartaAction(msg);
        opponentClientServer.makeAnAction(spartaAction);

        Peli.tauko(1500);
        System.out.println("Hävisit taistelun. Oot ihan nolo.");

        int xp = (int)  ( 0.5 * ((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[0]));
        int raha = (int) (0.5 * ((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[1]));

        System.out.println("\nSait " + xp + " xp:ta!");
        System.out.println("Sait " + raha + " rahaa!");
        pelaaja.lisaaXP(xp);
        pelaaja.lisaaRaha(raha);

        pelaaja.laskeArvoa();
        ownClient.closeGame();
    }



    /*  metodeja joita vastustaja kutsuu SpartaClient.makeAction() kautta */
    public void attack(SpartaAction sa) {
        System.out.println(sa.getMessage());
        this.pelaaja.asetaElama(this.pelaaja.annaElama() - (int) sa.getDamage());
    }

    public void win(SpartaAction sa){
        System.out.println(sa.getMessage());
        Peli.tauko(1500);
        System.out.println("Voitit taistelun. Oot ihan paras Sparta taistelija.");

        int xp = (int)  (((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[0]));
        int raha = (int) (((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[1]));

        System.out.println("\nSait " + xp + " xp:ta!");
        System.out.println("Sait " + raha + " rahaa!");
        pelaaja.lisaaXP(xp);
        pelaaja.lisaaRaha(raha);

        pelaaja.lisaaArvo();
        ownClient.closeGame();
    }
}
