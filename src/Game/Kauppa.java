package Game;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Miquel on 10.2.2016.
 */
public class Kauppa {

    private Peli peli;
    private ArrayList<Tavara> tavarat = new ArrayList<Tavara>();
    private Scanner sc;
    private TavaraTehdas tt;

    private boolean poistu = false;
    private Tila tila;

    Valikko pValikko;

    //Kyseistä tilaa kuvaava tilamuuttuja
    public enum Tila {
        VALIKKO, OSTA, MYY
    }

    public Kauppa(Peli peli, Pelaaja pelaaja, Scanner sc) {
        this.peli = peli;
        this.sc = sc;
        tt = new TavaraTehdas();

        //lataa lista kaikista tavaroista
        tavarat = tt.luoTavarat();
        this.tila = Tila.VALIKKO;

        //Alustaa kaupan päävalikon
        pValikko = new Valikko(sc);
        pValikko.lisaaVaihtoehto(new Vaihtoehto("Osta tavaroita") {
            @Override
            public void suorita() {
                vaihdaTila(Tila.OSTA);
            }
        });
        pValikko.lisaaVaihtoehto(new Vaihtoehto("Myy tavaroita") {
            @Override
            public void suorita() {
                vaihdaTila(Tila.MYY);
            }
        });
        pValikko.lisaaVaihtoehto(new Vaihtoehto("Poistu kaupasta") {
            @Override
            public void suorita() {
                poistu();
            }
        });
    }

    /*
        Kauppa aktivoituu, eli se on kutsuttu pelin päävalikosta.
        Siirrytään kaupan looppiin jossa pysytään kunnes poistumista kutsuttu.
        Kaupassa 3 tilaa; VALIKKO, OSTA ja MYY joille on tilamuuttuja tila.
     */
    public void aktivoi() {
        poistu = false;
        while (!poistu) {
            System.out.println();
            switch (tila) {
                case VALIKKO:
                    //Näytä valikko
                    tulostaKulta();
                    try {
                        pValikko.tulosta();
                        pValikko.valitse();
                    } catch (ValikkoPoikkeus vp) {
                        System.out.println(vp.getMessage());
                    }
                    break;
                case OSTA:
                    //Näytä ostovalikko1

                    tulostaKulta();
                    Valikko v = new Valikko(sc);
                    for (Tavara t : tavarat) {
                        //Luo jokaista tavaraa kohden oma vaihtoehto
                        if (t.annaLaatu() >= peli.annaEdistyminen() - 1 && t.annaLaatu() <= peli.annaEdistyminen()) {
                            v.lisaaVaihtoehto(new Vaihtoehto(t.toString()) {
                                @Override
                                public void suorita() {
                                    yritaOstoa(t);
                                }
                            });
                        }
                    }
                    v.lisaaVaihtoehto(new Vaihtoehto("Poistu") {
                        @Override
                        public void suorita() {
                            vaihdaTila(Tila.VALIKKO);
                        }
                    });
                    try {
                        v.tulosta();
                        v.valitse();
                    } catch (ValikkoPoikkeus vp) {
                        System.out.println(vp.getMessage());
                    }
                    break;
                case MYY:
                    Valikko vm = new Valikko(sc);
                    ArrayList<Tavara> tavarat = haePelaajanTavarat();
                    for(Tavara t : tavarat){
                        vm.lisaaVaihtoehto(new Vaihtoehto(t.toString()){
                            @Override
                            public void suorita() {
                                yritaMyyda(t);
                            }
                        });
                    }
                    vm.lisaaVaihtoehto(new Vaihtoehto("Poistu"){
                        @Override
                        public void suorita() {
                            vaihdaTila(Tila.VALIKKO);
                        }
                    });
                    vm.tulosta();
                    vm.pakotaValinta();
            }
        }
    }


    private ArrayList<Tavara> haePelaajanTavarat(){
        ArrayList<Tavara> tavarat = new ArrayList<Tavara>();
        Ase a = peli.annaPelaaja().annaAse();
        if(a != null){
            tavarat.add(a);
        }

        for(int i = 0; i < 5; i++){
            Panssari p = peli.annaPelaaja().annaPanssari(i);
            if(p != null){
                tavarat.add(p);
            }
        }
        return tavarat;
    }

    //Yrittää tavaran ostoa. Tarkistaa että pelaajalla on tarpeeksi rahaa.
    private void yritaOstoa(Tavara t) {
        if (peli.annaPelaaja().annaRaha() >= t.annaHinta()) {
            if (t instanceof Ase) {
                if(peli.annaPelaaja().lisaaAse((Ase) t)) {
                    System.out.println("Ostit aseen " + t.annaNimi());
                }
            } else if (t instanceof Panssari) {
                if (peli.annaPelaaja().lisaaPanssari((Panssari) t)) {
                    System.out.println("Ostit panssarin " + t.annaNimi());
                }
            }
        } else {
            System.out.println("Ei tarpeeksi rahaa ostamiseen!");
        }
        Peli.tauko(1000);
    }

    private void yritaMyyda(Tavara t){
        if(t instanceof Ase){
            peli.annaPelaaja().poistaAse();
        } else if(t instanceof Panssari){
            peli.annaPelaaja().poistaPanssari(((Panssari) t).annaTyyppi());
        }
        System.out.println("Myit esineen " + t.annaNimi());
        Peli.tauko(1000);
    }

    //vaihtaa kaupan tilaa
    public void vaihdaTila(Tila tila) {
        this.tila = tila;
    }

    public void listaaTavarat() {
        for (Tavara t : tavarat) {
            System.out.println(t);
        }
    }

    public void poistu() {
        this.poistu = true;
    }

    public void tulostaKulta() {
        System.out.println("Kultaa: " + peli.annaPelaaja().annaRaha());
    }

}