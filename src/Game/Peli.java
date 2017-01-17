package Game;

import Client.SpartaClient;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Yhta pelia kuvaava luokka.
 */
public class Peli {
    private Scanner sc;

    private boolean exit = false;

    private Aloitus aloitus;
    private Pelaaja pelaaja;

    private VastustajaTehdas vt;
    private Valikko valikko;
    private Kauppa kauppa;

    protected static int edistyminen;


    //uuden pelin luomiseen tarkoitettu konstruktori
    public Peli(Aloitus a, Scanner sc) {
        this.aloitus = a;
        this.sc = sc;
        alustus();
        System.out.println("\nAloitetaan uusi peli.");
        System.out.print("Uuden sankarin nimi: ");
        pelaaja = new Pelaaja(sc.next());
        this.edistyminen = 0;

        System.out.println("Erkki Kaila ystävineen on varastanut Viimeisen Spartan!\nTehtävänäsi on tuhota hanen imperiuminsa ja saada Viimeinen Sparta takaisin.");
        Peli.tauko(2000);
        System.out.println("\nEtenet pelissä taistelemalla järjestyksessä Erkin ystaviä vastaan.\nVastustajat muuttuvat entista vaikeammiksi pelin edetessä.\nOnneksi voit ostaa itsellesi esineita kaupasta, jotka auttavat sinua taistelussa.");
        Peli.tauko(4000);
    }

    //tallennetun pelin luomiseen tarkoitettu konstruktori
    public Peli(Aloitus a, Scanner sc, Tallennus t) {
        this.aloitus = a;
        this.sc = sc;
        alustus();
        System.out.println("\nJatketaan tallennettua peliä.");
        pelaaja = t.annaPelaaja();
        this.edistyminen = t.annaEdistyminen();
    }

    /**
     * Alustaa tarvittavat oliot pelin toimimista varten
     */
    public void alustus() {
        kauppa = new Kauppa(this, pelaaja, sc);
        vt = new VastustajaTehdas();
        valikko = new PaaValikko(this, sc);
    }


    /**
     * Aloittaa peliloopin
     */
    public void aloita() {
        while (!exit) {
            try {
                valikko.tulosta();
                valikko.valitse();
            } catch (ValikkoPoikkeus p) {
                System.out.println(p.getMessage());
            }
        }
    }


    /**
     * Taistelun hoitava metodi
     */
    public void uusiTaistelu() {
        //hae vastustaja edistymisen perusteella
        Vastustaja va = vt.annaVastustaja(edistyminen);
        Taistelu t = new Taistelu(pelaaja, va);

        System.out.println("Saavut areenalle ja kohtaat seuraavan vastustajasi.");
        Peli.tauko(1500);
        System.out.println("Hän on " + va.annaNimi() + " ja hän on valmis tuhoamaan sinut.\n");
        Peli.tauko(1500);

        //Jos pelaaja voitti taistelun
        if (t.taistele()) {
            pelaaja.hahmo_Elama = pelaaja.annaMaxElama();
            this.edistyminen++;
            if (edistyminen > 10) {
                System.out.println("Erkki Kaila on päihitetty. Viimeinen Sparta on vihdoinkin sinun!");
                Peli.tauko(2000);
                System.out.println("Lähestyt tätä myyttistä jumalten lahjaa ja otat sen käsiisi.");
                Peli.tauko(1500);
                System.out.println("Nostat Spartan kohti taivaita ja lausut:");
                Peli.tauko(2500);
                System.out.println("Kupit eteen.");
                Peli.tauko(2500);
                System.out.println("Kupittaa.");
                valikko = new PaaValikko(this, sc);
                Peli.tauko(2000);
            }
            int xp = (int) (((0.8 + (Math.random() * 0.4)) * va.annaXP()));
            int raha = (int) (((0.8 + (Math.random() * 0.4)) * va.annaRaha()));

            System.out.println("\nSait " + xp + " xp:ta!");
            System.out.println("Sait " + raha + " rahaa!");
            pelaaja.lisaaXP(xp);
            pelaaja.lisaaRaha(raha);
        } else {
            //jos pelaaja havisi poistu tasta pelista takaisin aloitukseen
            System.out.println("Maailma pimenee silmissäsi ja näet tunnelin päässä taivaallisen Spartan.");
            Peli.tauko(1500);
            System.out.println("Peli loppui");
            this.exit();
        }
    }

    public void siirryKauppaan() {
        kauppa.aktivoi();
    }

    public void aloitaMoninpeli() {
        SpartaClient spartaClient;
        try {
            spartaClient = new SpartaClient(sc);
        } catch (ConnectException ce) {
            System.out.println("Server is not responding :(");
            ce.printStackTrace();
            return;
        } catch (InterruptedException ie){
            System.out.println("Moninpelihaku keskeytetty");
            return;
        } catch (Exception e) {
            System.out.println("Program is not working right. Pls fix.");
            e.printStackTrace();
            return;
        }

        try {
            spartaClient.createConnectionToOpponent();
        } catch (Exception e){
            System.out.println("Yhteys vastustajaan katkesi");
        }
    }

    //Sulkee pelin
    public void exit() {
        this.exit = true;
    }

    public Aloitus annaAloitus() {
        return aloitus;
    }

    public Pelaaja annaPelaaja() {
        return pelaaja;
    }

    public int annaEdistyminen() {
        return edistyminen;
    }

    public VastustajaTehdas annaVt() {
        return vt;
    }

    //Keskeyttaa prosessin suorittamisen hetkeksi, Kaytetaan sulavoittamaan kayttaliittymaa
    public static void tauko(int aika) {
        try {
            Thread.sleep(aika);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}