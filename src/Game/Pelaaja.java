package Game;



//Kasittelee pelaajahahmoon liittyvia asioita.
/**
 *
 * @author Juho
 * Hahmo-luokan periva luokka Pelaaja, jotka kasittelee pelaajahahmoon liittyvia asioita.
 */
public class Pelaaja extends Hahmo {
    private int pelaaja_MaxElama;
    private int pelaaja_XP;
    private int pelaaja_Taso;
    private int vaadittuMaaraSeuraavaanTasoon = 1000;
    private int pelaaja_Raha;
    private int pelaaja_Arvo;

    //Hahmoilla voi olla viisi panssarin osaa ja yksi ase.
    private Panssari[] armor = new Panssari[5];
    private Ase ase;

    //Luo pelaajan oletusarvoilla.
    //!!alkaa luoko pelaajaa tata metodia kayttaen!!
    public Pelaaja() {
        super();
        this.pelaaja_MaxElama = 100;
        this.pelaaja_XP = 0;
        this.pelaaja_Taso = 1;
        this.pelaaja_Raha = 0;
        this.pelaaja_Arvo = 0;
    }

    //Luo pelaajan annetulla nimella ja asettaa sille oletusstatsit.
    /**
     * Tama metodi luo pelaajahahmon uutta pelia aloitettaessa.
     * Metodi asettaa uudelle pelaajalle ennaltamaaratyt tilastot.
     * @param nimi Ainoa parametri, joka pelaajalle tarvitsee antaa sita luotaessa, on pelaajanimi.
     */
    public Pelaaja(String nimi) {
        this.hahmo_Nimi = nimi;
        this.hahmo_Elama = 100;
        this.pelaaja_MaxElama = 100;
        this.hahmo_Voima = 10;
        this.hahmo_Suoja = 10;
        this.hahmo_Tarkkuus = 10.0;
        this.hahmo_CritRate = 0.10;
        this.pelaaja_XP = 0;
        this.pelaaja_Taso = 1;
        this.pelaaja_Raha = 0;
        this.pelaaja_Arvo = 0;
    }


    //Metodi, jolla voi lisata pelaajalle aseen.
    /**
     * Metodia hyadynnetaan kaupassa, kun asetta ostetaan.
     * Tama metodi myy vanhan aseen pois, jos pelaajalla on sellainen. Jos asetta ei ole, uusi ase voidaan asettaa pelaajalle ilman lisatoimia.
     *
     * @param ase Maarittaa pelaajalle lisattavan aseen. Metodille annetaan Ase-tyyppinen olio.
     * @return Metodi palauttaa false, jos aseen lisaaminen ei onnistu.
     */
    public boolean lisaaAse(Ase ase) {
        if(ase.anna2h() == false) {  //Jos lisattava ase ei ole 2h, kilvella ei ole valia
            if(this.ase == null) {  //Katsotaan onko ase null
                this.ase = ase;
                suoritaAseMuutokset(ase);

            } else {  //Jos ase on jo
                poistaAse();  //Se pitaa poistaa ennen uuden lisaysta
                this.ase = ase;
                suoritaAseMuutokset(ase);
            }
        } else {  //Jos lisattava ase on 2h, pitaa katsoa, onko kilpi
            if(this.armor[4] != null) {  //Jos on
                System.out.println(("Käsissäsi on kilpi. Et voi käyttää kahden käden asetta!"));
                return false;  //Niin uutta asetta  ei voi lisata
            } else {  //Muutoin sama setti kuin ylhaallakin
                if(this.ase == null) {
                    this.ase = ase;
                    suoritaAseMuutokset(ase);

                } else {
                    poistaAse();
                    this.ase = ase;
                    suoritaAseMuutokset(ase);
                }
            }
        }
        return true;
    }

    //Metodi, jolla voi lisata pelaajalle panssarin.
    /**
     * Metodia hyadynnetaan kaupassa, kun panssaria ostetaan.
     * Tama metodi myy vanhan panssarin pois, jos pelaajalla on kyseista tyyppia oleva panssari. Jos ei ole, uusi panssari voidaan asettaa pelaajalle ilman lisatoimia.
     *
     * @param panssari Maarittaa pelaajalle lisattavan panssarin. Metodille annetaan Panssari-tyyppinen olio.
     * @return Metodi palauttaa false, jos panssarin lisaaminen ei onnistu.
     */
    public boolean lisaaPanssari(Panssari panssari) {
        //Mikali pelaajalla ei ole asetta, ei tarvitse tarkistaa, voiko kilven asettaa paalle.
        if(this.ase == null) {
            if(armor[panssari.annaTyyppi()] == null) {  //Jos armorslotti on tyhja, uusi esine voidaan asettaa suoraan
                armor[panssari.annaTyyppi()] = panssari;

                suoritaArmorMuutokset(panssari);

            } else {
                poistaPanssari(panssari.annaTyyppi());  //Jos se ei ole tyhja, vanha pitaa myyda ensin.
                armor[panssari.annaTyyppi()] = panssari;

                suoritaArmorMuutokset(panssari);
            }


        } else {  //Jos pelaajalla on ase, pitaa kilpea lisatessa tarkistaa, etta ase ei ole kaksikatinen.
            //Kahden kaden aseella ei ole merkitysta, jos osa ei ole kilpi.
            if(panssari.annaTyyppi() == 0 || panssari.annaTyyppi() == 1 || panssari.annaTyyppi() == 2 || panssari.annaTyyppi() == 3) {
                if(armor[panssari.annaTyyppi()] == null) {  //Jos armorslotti on tyhja, uusi esine voidaan asettaa suoraan
                    armor[panssari.annaTyyppi()] = panssari;

                    suoritaArmorMuutokset(panssari);

                } else {
                    poistaPanssari(panssari.annaTyyppi());  //Jos se ei ole tyhja, vanha pitaa myyda ensin.
                    armor[panssari.annaTyyppi()] = panssari;

                    suoritaArmorMuutokset(panssari);
                }


            } else { //Jos uusi esine on kilpi...
                if(this.ase.anna2h() == true) {  //Jos on 2h, kilpea ei voi kayttaa.
                    System.out.println("Käsissäsi on kahden käden ase. Et voi käyttää kilpeä!");
                    return false;
                } else {
                    if(armor[panssari.annaTyyppi()] == null) {  //Jos armorslotti on tyhja, uusi esine voidaan asettaa suoraan
                        armor[panssari.annaTyyppi()] = panssari;

                        suoritaArmorMuutokset(panssari);

                    } else {
                        poistaPanssari(panssari.annaTyyppi());  //Jos se ei ole tyhja, vanha pitaa myyda ensin.
                        armor[panssari.annaTyyppi()] = panssari;

                        suoritaArmorMuutokset(panssari);
                    }
                }
            }
        }
        return true;
    }

    //Kaikki nelja allaolevaa metodia kasvattavat tai vahentavat pelaajan statseja, kun esineita poistetaan tai lisataan.
    //Ilmestyneet varusteupdatessa.

    //Tama metodi suorittaa aseen aiheuttamat stat muutokset
    /**
     * Suorittaa muutokset pelaajan tilastoihin, kun ase lisataan. Metodi lisaaAse(Ase ase) kutsuu tata metodia.
     * Metodi vahentaa pelaajan rahaa ja lisaa kaikkiin muihin statseihin esineessa maaritellyt arvot.
     * @param ase Metodi lisaaAse(Ase ase) kutsuu tata metodia siihen annetulla Ase-tyyppisella oliolla.
     */
    public void suoritaAseMuutokset(Ase ase) {
        this.pelaaja_Raha = this.pelaaja_Raha - ase.annaHinta();
        this.hahmo_Elama = this.hahmo_Elama + ase.tavara_Elama();
        this.pelaaja_MaxElama = this.pelaaja_MaxElama + ase.tavara_Elama();
        this.hahmo_Voima = this.hahmo_Voima + ase.annaVoima();
        this.hahmo_Suoja = this.hahmo_Suoja + ase.annaSuoja();
        this.hahmo_Tarkkuus = this.hahmo_Tarkkuus + ase.annaTarkkuus();
        this.hahmo_CritRate = this.hahmo_CritRate + ase.annaCritRate();
    }

    //Tama taasen panssarin aiheuttamat stat muutokset
    /**
     * Suorittaa muutokset pelaajan tilastoihin, kun panssari lisataan. Metodi lisaaPanssari(Panssari panssari) kutsuu tata metodia.
     * Metodi vahentaa pelaajan rahaa ja lisaa kaikkiin muihin statseihin esineessa maaritellyt arvot.
     * @param panssari Metodi lisaaPanssari(Panssari panssari) kutsuu tata metodia siihen annetulla Panssari-tyyppisella oliolla.
     */
    public void suoritaArmorMuutokset(Panssari panssari) {
        this.pelaaja_Raha = this.pelaaja_Raha - panssari.annaHinta();
        this.hahmo_Elama = this.hahmo_Elama + panssari.tavara_Elama();
        this.pelaaja_MaxElama = this.pelaaja_MaxElama + panssari.tavara_Elama();
        this.hahmo_Voima = this.hahmo_Voima + panssari.annaVoima();
        this.hahmo_Suoja = this.hahmo_Suoja + panssari.annaSuoja();
        this.hahmo_Tarkkuus = this.hahmo_Tarkkuus + panssari.annaTarkkuus();
        this.hahmo_CritRate = this.hahmo_CritRate + panssari.annaCritRate();
    }

    //Poistaa pelaajan kaytassa olevan panssarin, vahentaen sen antamat statsit pois ja palauttaen osan sen ostohinnasta.
    /**
     * Tata metodia kaytetaan, kun pelaaja myy panssarin kaupassa.
     * Metodi palauttaa pelaajalle puolet esineen ostohinnasta ja vahentaa pelaajan statseja esineessa maaritettyjen arvojen mukaisesti.
     * @param index Pelaajalla on attribuuttina taulukko armor, jossa on ruudut valilla 0-4. Index maarittaa ruudun, joka tyhjennetaan. Index ja Panssari-luokassa maaritelty attribuutti Tyyppi jakavat samat luvut.
     */
    public void poistaPanssari(int index) {
        this.pelaaja_Raha = this.pelaaja_Raha + (armor[index].annaHinta())/2;
        this.hahmo_Elama = this.hahmo_Elama - armor[index].tavara_Elama();
        this.pelaaja_MaxElama = this.pelaaja_MaxElama - armor[index].tavara_Elama();
        this.hahmo_Voima = this.hahmo_Voima - armor[index].annaVoima();
        this.hahmo_Suoja = this.hahmo_Suoja - armor[index].annaSuoja();
        this.hahmo_Tarkkuus = this.hahmo_Tarkkuus - armor[index].annaTarkkuus();
        this.hahmo_CritRate = this.hahmo_CritRate - armor[index].annaCritRate();
        armor[index] = null;
    }

    /**
     * Poistaa pelaajan kaytassa olevan aseen.
     * Metodi palauttaa pelaajalle puolet esineen ostohinnasta ja vahentaa pelaajan statseja esineessa maaritettyjen arvojen mukaisesti.
     */
    public void poistaAse() {
        this.pelaaja_Raha = this.pelaaja_Raha + (this.ase.annaHinta())/2;
        this.hahmo_Elama = this.hahmo_Elama - this.ase.tavara_Elama();
        this.pelaaja_MaxElama = this.pelaaja_MaxElama - this.ase.tavara_Elama();
        this.hahmo_Voima = this.hahmo_Voima - this.ase.annaVoima();
        this.hahmo_Suoja = this.hahmo_Suoja - this.ase.annaSuoja();
        this.hahmo_Tarkkuus = this.hahmo_Tarkkuus - this.ase.annaTarkkuus();
        this.hahmo_CritRate = this.hahmo_CritRate - this.ase.annaCritRate();
        this.ase = null;
    }

    //Antaa pelaajan kayttamat esineet Ase ja Panssari-tyyppisina olioina.
    /**
     * @return Palauttaa pelaajan kaytassa olevan aseen Ase-tyyppisena oliona.
     */
    public Ase annaAse() {
        return ase;
    }
    /**
     *
     * @param index Maarittaa, minka tyyppinen panssari palautetaan pelaajalle.
     * @return Palauttaa indexin maaraaman panssarin Panssari-tyyppisena oliona.
     */
    public Panssari annaPanssari(int index) {
        return armor[index];
    }




    /**
     * Metodi lisaa pelaajalle kokemuspisteita (XP).
     * Joka kerta, kun metodia kutsutaan, se tutkii etta riittaaka saatu XP pelaajan tason kasvamiseen.
     * Tason kasvaminen kasvattaa pelaajan tilastoja, mukaan lukien elamapisteet.
     * Pelaajan taso voi kasvaa monta kertaa, jos saadun XP:n maara on riittavan suuri.
     * @param XP Lisattavien kokemuspisteiden maara.
     */
    public void lisaaXP(int XP) {
        this.pelaaja_XP = this.pelaaja_XP + XP;  //Lisaa (tai vahentaa!) pelaajan kokemuspisteita


        //Level-up toiminto:
        if (this.pelaaja_XP > vaadittuMaaraSeuraavaanTasoon) {  //Jos uusi level on suurempi kuin vanha
            while(true) {  //Level up toistetaan tason muuttumismaaran mukaisesti, tama toimii ikuisen loopin avulla.
                //Kasvatetaan tasoa
                this.pelaaja_Taso = this.pelaaja_Taso +1;

                //Level-up kertoo, miten statsisi kasvavat:
                System.out.println("\nHahmosi taso kasvoi! Olet nyt tasolla: " + this.pelaaja_Taso + "\nTilastosi kasvoivat seuraavasti:");
                System.out.println("Maksimielämä: " + (6 + (this.pelaaja_Taso/8)*13));
                System.out.println("Voima: " + (1 + this.pelaaja_Taso/4));
                System.out.println("Suoja: " + (1 + this.pelaaja_Taso/5));
                System.out.println("Tarkkuus: " + (1.0 + this.pelaaja_Taso/4.0));
                System.out.println("Kritikaalisuus: " + this.pelaaja_Taso/300.0);

                //Ja suorittaa kertomansa muutokset hahmon taitoihin:
                this.hahmo_Elama = this.hahmo_Elama + 6 + (this.pelaaja_Taso/8)*13;
                this.pelaaja_MaxElama = this.pelaaja_MaxElama + 6 + (this.pelaaja_Taso/8)*13;
                this.hahmo_Voima = this.hahmo_Voima + 1 + this.pelaaja_Taso/4;
                this.hahmo_Suoja = this.hahmo_Suoja + 1 + this.pelaaja_Taso/5;
                this.hahmo_Tarkkuus = this.hahmo_Tarkkuus + 1.0 + this.pelaaja_Taso/4.0;
                this.hahmo_CritRate = this.hahmo_CritRate + this.pelaaja_Taso/300.0;

                //Alustetaan seuraavan level-upin XP raja
                vaadittuMaaraSeuraavaanTasoon = (vaadittuMaaraSeuraavaanTasoon/5)*8;  //Laskee uuden int rajan, jolla saadaan seuraava level up

                if(this.pelaaja_XP < vaadittuMaaraSeuraavaanTasoon) {  //Taman avulla ikuinen looppi loppuu
                    break;
                }
            }
        }
    }

    /**
     * Generoi XP- ja rahapalkinnot, kun kyseinen pelaaja paihitetaan taistelussa. (Online)
     */
    public int[] voittoPalkkiot() {
        int[] palkkiot = new int[2];	//Sisaltaa pelaajan antaman XP:n ja rahan

        palkkiot[0] = this.pelaaja_XP/20*this.pelaaja_Arvo;		//Generoi saadun XP:n maaran
        palkkiot[1] = this.pelaaja_XP/200*this.pelaaja_Arvo;	//Generoi saadun rahan maaran

        return palkkiot;
    }

    //Metodit, joilla voi muuttaa pelaajan statseja (kaikki nama voivat myas vahentaa niita)
    /**
     * Pelaajalla on kaksi erityyppista elamaa.
     * hahmo_Elama on se, joka laskee taisteluiden aikana. pelaaja_MaxElama ei laske taistelussa.
     * Tama metodi palauttaa pelaajan elamapisteet takaisin maksimiin.
     */
    public void asetaMaxElama() {
        this.hahmo_Elama = this.pelaaja_MaxElama;  //Tama metodi palauttaa pelaajan elaman sen maksimiarvoon
    }
    /**
     * Metodilla voi lisata hahmon voimaa.
     * @param voima Lisattava voiman maara.
     */
    public void lisaaVoima(int voima) {
        this.hahmo_Voima = this.hahmo_Voima + voima;  //Tama metodi lisaa pelaajan voimaa
    }
    /**
     * Metodilla voi lisata hahmon suojaa.
     * @param suoja Lisattava suojan maara.
     */
    public void lisaaSuoja(int suoja) {
        this.hahmo_Suoja = this.hahmo_Suoja + suoja;  //Tama metodi lisaa pelaajan suojaa
    }
    /**
     * Metodilla voi lisata hahmon tarkkuutta.
     * @param tarkkuus Lisattava tarkkuuden maara.
     */
    public void lisaaTarkkuus(int tarkkuus) {
        this.hahmo_Tarkkuus = this.hahmo_Tarkkuus + tarkkuus;  //Tama metodi lisaa pelaajan tarkkuutta
    }
    /**
     * Metodilla voi lisata hahmon kritikaalisen osuman mahdollisuutta.
     * @param critrate Lisattava mahdollisuus.
     */
    public void lisaaCritRate(int critrate) {
        this.hahmo_CritRate = this.hahmo_CritRate + critrate;  //Tama metodi lisaa pelaajan crit ratea
    }
    /**
     * Metodilla voi lisata hahmon elamaa.
     * @param elama Lisattava elaman maara.
     */
    public void lisaaElama(int elama) {
        this.hahmo_Elama = this.hahmo_Elama + elama;  //Tama metodi lisaa pelaajan elamaa (voimme tarvita sita myahemmin)
        this.pelaaja_MaxElama = this.pelaaja_MaxElama + elama;
    }
    /**
     * Metodilla voi lisata pelaajan rahaa.
     * @param raha Lisattava rahamaara.
     */
    public void lisaaRaha(int raha) {
        this.pelaaja_Raha = this.pelaaja_Raha + raha;
    }

    /**
     * Metodi kasvattaa pelaajan arvoa yhdella, kunhan se on alle 25.
     */
    public void lisaaArvo() {
        if(this.pelaaja_Arvo < 25) {
            this.pelaaja_Arvo = this.pelaaja_Arvo +1;
            System.out.println("Hahmosi arvo kasvoi yhdellä!");
        }
    }

    /**
     * Metodi laskee pelaajan arvoa yhdella, kunhan se on yli 0.
     */
    public void laskeArvoa() {
        if(this.pelaaja_Arvo > 0) {
            this.pelaaja_Arvo = this.pelaaja_Arvo -1;
            System.out.println("Hahmosi arvo laski yhdellä.");
        }
    }

    /**
     * @return Palauttaa pelaajalla olevan rahan maaran.
     */
    public int annaRaha() {
        return this.pelaaja_Raha;
    }
    /**
     * @return Palauttaa pelaajan maksimaalisen elaman.
     */
    public int annaMaxElama() {
        return this.pelaaja_MaxElama;
    }


    public int annaTaso() {
        return this.pelaaja_Taso;
    }

    /**
     * Tama metodi palauttaa pelaajahahmon tiedot luettavassa muodossa.
     */
    public String toString() {
        String s1 = hahmo_Nimi + "n tilastot:\n\nTaso: " + pelaaja_Taso + "\nKokemuspisteet: " + pelaaja_XP + " / Raja tason kasvuun: " + vaadittuMaaraSeuraavaanTasoon + "\nRaha: " + pelaaja_Raha + "\nArvo: " + pelaaja_Arvo + "\nMaksimielama: " + pelaaja_MaxElama + "\nVoima: " + hahmo_Voima + "\nSuoja: " + hahmo_Suoja + "\nTarkkuus: " + hahmo_Tarkkuus + "\nKritikaalisuus: " + hahmo_CritRate + "\n";

        String s2 = "";
        if(this.ase != null) {
            s2 = "\nKäytössä oleva Ase: " + ase.annaNimi();
        }

        String s3 = "";
        for (int i=0; i < 5; i++) {
            if (armor[i] != null) {
                s3 = s3 + "\nKäytössä oleva " + Panssari.nimea(i) + ": " + armor[i].annaNimi();
            }
        }
        return (s1 + s2 + s3);
    }
}


