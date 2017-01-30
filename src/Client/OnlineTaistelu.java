package Client;

import Game.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.Scanner;
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
    private int spartaBaseStr = 10;
    private int spartaStr = 0;
    private int spartaDamage = 0;
    private Hahmo opponentHahmo;
    private boolean crithit = false;
    private boolean hits = false;

    private Valikko taisteluValikko;

    public OnlineTaistelu(SpartaClient ownClient, ClientServerInterface opponentClientServer, boolean oneWhoStarts, Pelaaja pelaaja, Scanner sc) {

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
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("Juo Spartaaa!!!") {
            @Override
            public void suorita() {
                drinkSparta();
            }
        });
        taisteluValikko.lisaaVaihtoehto(new Vaihtoehto("Ota vähän lepiä") {
            @Override
            public void suorita() {
                skipTurn();
            }
        });
    }

    public void startIntroduce() throws RemoteException {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        if (pelaaja.elossa()) {
            //ask user input
            taisteluValikko.tulosta();
            taisteluValikko.pakotaValinta();

        } else {
            die();
        }
        this.myTurn = false;
        System.out.println("Opponents turn.");
    }

    public boolean checkTurn() {
        return myTurn;
    }

    public void setTurn(boolean turn) {
        this.myTurn = turn;
    }

    public void opponentIntroduced() {
        this.opponentHasIntroduced = true;
    }

    public boolean opponentHasIntroduced() {
        return opponentHasIntroduced;
    }

    public boolean iHaveIntroduced() {
        return iHaveIntroduced;
    }

    public void updateStats(Hahmo hahmo) {
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

            damage = ((this.pelaaja.annaVoima() + spartaStr) / (((0.2 + (Math.random() * 0.8)) * this.opponentHahmo.annaSuoja())));


            if (crithit) {
                msg = (this.pelaaja.annaNimi() + " iskee viholliseen " + this.opponentHahmo.annaNimi() + " kriittistä vahinkoa: " + (int) damage + "!");
                damage = (damage) + (Math.random() * (1.5 * damage));
            } else {
                msg = (this.pelaaja.annaNimi() + " iskee vihollista " + this.opponentHahmo.annaNimi() + " aiheuttaen vahinkoa: " + (int) damage);
            }
        }

        System.out.println(msg);
        SpartaAction sa = new SpartaAction((int) damage, crithit, msg);
        makeAnAction(sa);

    }

    public void die() throws RemoteException {

        String msg = pelaaja.annaNimi() + " kaatuu maahan tuskissaan.";
        System.out.println(msg);
        SpartaAction spartaAction = new SpartaAction(msg);
        opponentClientServer.makeAnAction(spartaAction);

        Peli.tauko(1500);
        System.out.println("Hävisit taistelun. Oot ihan nolo.");

        int xp = (int) (0.5 * ((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[0]));
        int raha = (int) (0.5 * ((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[1]));

        System.out.println("\nSait " + xp + " xp:ta!");
        System.out.println("Sait " + raha + " rahaa!");
        pelaaja.lisaaXP(xp);
        pelaaja.lisaaRaha(raha);

        pelaaja.laskeArvoa();
        ownClient.closeGame();
    }

    public void drinkSparta() {
        spartaStr += spartaBaseStr;
        spartaDamage = (int) (9 * Math.random() + 1);
        System.out.println("Sparta antaa sinulle " + spartaBaseStr + " strenaa, mutta saat " + spartaDamage + " vahinkoa.");
        spartaBaseStr *= 2;
        pelaaja.lisaaElama(spartaDamage * -1);

        SpartaAction sa = new SpartaAction();
        sa.setTypeOfAction(1);
        sa.setMessage(pelaaja.annaNimi() + " joi spartan ja hänen voimansa kasvoi!");
        makeAnAction(sa);
    }

    public void skipTurn() {
        SpartaAction sa = new SpartaAction();
        sa.setMessage(pelaaja.annaNimi() + " tuhlaa vuoronsa.");
        System.out.println("Lepäät.");
        sa.setTypeOfAction(3);
        makeAnAction(sa);
    }


    private boolean makeAnAction(SpartaAction sa) {
        for (int i = 0; i < 5; i++) {
            try {
                opponentClientServer.makeAnAction(sa);
                return true;
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
        return false;
    }


    /*  metodeja joita vastustaja kutsuu SpartaClient.makeAction() kautta */
    public void attack(SpartaAction sa) {
        System.out.println(sa.getMessage());
        this.pelaaja.asetaElama(this.pelaaja.annaElama() - (int) sa.getDamage());
    }

    public void win(SpartaAction sa) {
        System.out.println(sa.getMessage());
        Peli.tauko(1500);
        System.out.println("Voitit taistelun. Oot ihan paras Sparta taistelija.");

        int xp = (int) (((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[0]));
        int raha = (int) (((0.8 + (Math.random() * 0.4)) * pelaaja.voittoPalkkiot()[1]));

        System.out.println("\nSait " + xp + " xp:ta!");
        System.out.println("Sait " + raha + " rahaa!");
        pelaaja.lisaaXP(xp);
        pelaaja.lisaaRaha(raha);

        pelaaja.lisaaArvo();
        ownClient.closeGame();
    }
}
