package Game;

// Versio 2.027!
/**
 * @author Juho
 * Ase-alaluokka Tavaraa varten.
 * Mallintaa pelissa kaytettavia aseita.
 */

public class Ase extends Tavara {
    protected int ase_Tyyppi;  //Aseiden tyypit: 0=Tikari, 1=Miekka, 2=Nuija, 3=2h miekka, 4=Keihas. Pelillisesti ei mitaan eroa, mutta aseet antavat erilaisia statseja.
    protected boolean ase_2h;  //Jos true, ase on kaksikatinen eika kilpea voi kayttaa.

    //Luo oletusmiekan.
    public Ase() {
        super();
        this.ase_Tyyppi = 1;
        this.ase_2h = false;
    }

    /**
     * Luo aseen annettujen tietojen perusteella.
     * @param laatu  Maarittaa tason, jolla kyseinen ase ilmestyy.
     * @param nimi  Aseen nimi.
     * @param hinta  Aseen ostohinta.
     * @param elama  Aseen antaman elaman maara (useimmiten nolla).
     * @param voima  Aseen antaman voiman maara.
     * @param suoja  Aseen antaman suojan maara.
     * @param tarkkuus  Aseen antama tarkkuus.
     * @param CritRate  Aseen antama kritikaalisen osuman mahdollisuus.
     * @param tyyppi  Asetyyppi. Aseet jakavat samat mekaniikat, mutta eri asetyypeilla on erilaisia statseja.
     */
    public Ase(int laatu, String nimi, int hinta, int elama, int voima, int suoja, double tarkkuus, double CritRate, int tyyppi) {
        super(laatu, nimi, hinta, elama, voima, suoja, tarkkuus, CritRate);  //Kutsutaan ylaluokan metodia

        //Tyypin taytyy olla 0-4.
        if(tyyppi > 4 || tyyppi < 0) {
            this.ase_Tyyppi = 0;
            this.ase_2h = false;
            System.out.println("Aseen tyypin täytyy olla luku välillä 0-4!");

        } else {
            this.ase_Tyyppi = tyyppi;
            this.ase_2h = maarita2h(tyyppi);
        }
    }

    /**
     * Metodi maarittaa aseen luontivaiheessa, onko se kaksi- vai yksikatinen.
     * Konstruktori kutsuu tata metodia, kun ase luodaan.
     * @param tyyppi Konstruktori antaa aseen tyypin. Tyyppi maarittaa, onko ase kaksikatinen.
     * @return Palauttaa true, jos ase on kaksikatinen. Palauttaa false, jos ei.
     */
    public boolean maarita2h(int tyyppi) {
        if(tyyppi == 0 || tyyppi == 1 || tyyppi == 2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Palauttaa asetyypit (int 0-4) luettavassa muodossa kauppanakymaa varten.
     * @param tyyppi Aseen tyyppi lukuna, jonka avulla se nimetaan.
     * @return Palauttaa asetyypin merkkijonona.
     */
    public static String nimea(int tyyppi) {  //Palauttaa tyypit
        switch(tyyppi){
            case 0:
                return "Tikari";

            case 1:
                return "Miekka";

            case 2:
                return "Nuija";

            case 3:
                return "Kahden käden miekka";

            case 4:
                return "Keihäs";

            default:
                return "";

        }
    }

    //Havainnointimetodit
    /**
     * @return Palauttaa asetyypin lukuna.
     */
    public int annaTyyppi() {
        return ase_Tyyppi;
    }
    /**
     * @return Palauttaa aseen kaksikatisyyden (true/false).
     */
    public boolean anna2h() {
        return ase_2h;
    }
    /**
     * Palauttaa aseen tiedot luettavassa muodossa.
     * aarimmaisen tarkea kaupan luettavuuden kannalta.
     */
    public String toString(){
        String s1 = this.tavara_Nimi + " Hinta: " + this.tavara_Hinta + " Laatu: Taso " + this.tavara_Laatu + " Aseen tyyppi: " + nimea(this.ase_Tyyppi) + " Kaksikätinen: " + this.ase_2h + "\n    ";

        String s2 = "";
        if(this.tavara_Elama > 0) {
            s2 = "Elama: " + this.tavara_Elama + " ";
        }

        String s3 = "Voima: " + this.tavara_Voima + " Suoja: " + this.tavara_Suoja + " Tarkkuus: " + this.tavara_Tarkkuus + " Kritikaalisuus: " + this.tavara_CritRate + "\n";
        return s1 + s2 + s3;
    }
}