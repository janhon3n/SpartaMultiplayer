package Game;
// Versio 2.030!
/**
 * 
 * @author Juho
 * Panssari-alaluokka Tavaraa varten.
 * Mallintaa pelissä käytettäviä panssarin osia.
 */

public class Panssari extends Tavara {
    protected int panssari_Tyyppi;  //Panssareiden tyypit: 0=Päähine, 1=Panssari, 2=Kengät, 3=Hanskat, 4=Kilpi
    //Huom. mikäli panssarin tyyppi on 4, pelaaja ei voi käyttää sitä, mikäli ase_2h = true;

    //Luo oletuspanssarin.
    public Panssari() {
        super();
        this.panssari_Tyyppi = 1;
    }

    /**
     * Luo panssarin annettujen tietojen perusteella.
     * @param laatu  Määrittää tason, jolla kyseinen panssari ilmestyy.
     * @param nimi  Panssarin nimi.
     * @param hinta  Panssarin ostohinta.
     * @param elama  Panssarin antamien elämäpisteiden määrä (useimmiten nolla).
     * @param voima  Panssarin antaman voiman määrä (useimmiten vain hanskoissa ja kilvissä).
     * @param suoja  Panssarin antaman suojan määrä.
     * @param tarkkuus  Panssarin antama tarkkuus (useimmiten nolla).
     * @param CritRate  Panssarin antama kritikaalisen osuman mahdollisuus (useimmiten nolla).
     * @param tyyppi  Panssarin tyyppi. Panssarit jakavat samat mekaniikat, mutta tilastot riippuvat tyypistä.
     */
    public Panssari(int laatu, String nimi, int hinta, int elama, int voima, int suoja, double tarkkuus, double CritRate, int tyyppi) {
        super(laatu, nimi, hinta, elama, voima, suoja, tarkkuus, CritRate);

        //Tyypin täytyy olla 0-4.
        if(tyyppi > 4 || tyyppi < 0) {
            this.panssari_Tyyppi = 0;
            System.out.println("Panssarin tyyppi on luku välillä 0-4!");

        } else {
            this.panssari_Tyyppi = tyyppi;
        }
    }

    /**
     * Palauttaa panssarityypit (int 0-4) luettavassa muodossa kauppanäkymää varten.
     * @param tyyppi Panssarin tyyppi lukuna, jonka avulla se nimetään.
     * @return Palauttaa tyypin merkkijonona.
     */
    public static String nimea(int tyyppi) {  //Palauttaa tyypit
        switch(tyyppi){
            case 0:
                return "Päähine";

            case 1:
                return "Panssari";

            case 2:
                return "Kengät";

            case 3:
                return "Hanskat";

            case 4:
                return "Kilpi";

            default:
                return "";

        }
    }

    /**
     * @return Palauttaa panssarin tyypin lukuna.
     */
    public int annaTyyppi() {
        return panssari_Tyyppi;
    }
    /**
     * Tulostaa panssari-tyyppisen olion tiedot luettavassa muodossa.
     * Erittäin tärkeä kauppanäkymän luettavuuden kannalta.
     */
    public String toString(){
        String s1 = this.tavara_Nimi + " Hinta: " + this.tavara_Hinta + " Laatu: Taso " + this.tavara_Laatu + " Panssarin tyyppi: " + nimea(this.panssari_Tyyppi) + "\n    ";

        String s2 = "";
        if(this.tavara_Elama > 0) {
            s2 = "Elama: " + this.tavara_Elama + " ";
            ;
        }

        String s3 = "Voima: " + this.tavara_Voima + " Suoja: " + this.tavara_Suoja + "\n";

        String s4 = "";
        if(this.tavara_Tarkkuus > 0) {
            s4 = " Tarkkuus: " + this.tavara_Tarkkuus;
        }

        String s5 = "";
        if(this.tavara_CritRate > 0) {
            s5 = " Kritikaalisuus: " + this.tavara_CritRate;
        }
        return s1 + s2 + s3 + s4 + s5;
    }
}