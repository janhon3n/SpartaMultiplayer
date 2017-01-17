package Game;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Miquel on 8.3.2016.
 */
public class Aloitus {

    String tallennusFile = "tallennus.ser";
    Scanner sc;
    boolean exit = false;

    public Aloitus() {
        sc = new Scanner(System.in);
    }

    public void aloita() {
        System.out.println("Viimeinen Sparta");

        while (!exit) {
            Valikko v = new Valikko(sc);
            v.lisaaVaihtoehto(new Vaihtoehto("Aloita uusi peli") {
                @Override
                public void suorita() {
                    aloitaUusi();
                }
            });
            if (onTallennus()) {
                v.lisaaVaihtoehto(new Vaihtoehto("Lataa edellinen tallennus") {
                    @Override
                    public void suorita() {
                        try {
                            Peli peli = lataaPeli();
                            peli.aloita();
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Pelin lataus epäonnistui.");
                        }
                    }
                });
            }
            v.lisaaVaihtoehto(new Vaihtoehto("Poistu") {
                @Override
                public void suorita() {
                    exit();
                }
            });
            System.out.println();
            v.tulosta();
            v.pakotaValinta();

        }
    }


    public boolean onTallennus() {
        File f = new File(tallennusFile);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public void tallennaPeli(Peli peli) {
        Tallennus t = new Tallennus(peli.annaPelaaja(), peli.annaEdistyminen());
        try {
            FileOutputStream fileOut = new FileOutputStream(tallennusFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(t);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.out.println("Tallennus epäonnistui");
        }
    }

    public Peli lataaPeli() throws IOException, ClassNotFoundException {
        Tallennus t = null;
        FileInputStream fileIn = new FileInputStream(tallennusFile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        t = (Tallennus) in.readObject();
        in.close();
        fileIn.close();

        return new Peli(this, sc, t);
    }


    public void aloitaUusi() {
        Peli peli = new Peli(this, sc);
        peli.aloita();
    }

    public void exit() {
        this.exit = true;
    }

    public static void main(String[] args) {
        Aloitus a = new Aloitus();
        a.aloita();
    }
}