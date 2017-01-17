package Game;
import java.io.Serializable;
// Versio 2.006!
/**Luokka, joka kasittaa jokaisen tavaran, jota pelissa kaytetaan.
 * Paaluokka on Tavara, jossa on aliluokkina Ase seka Panssari.
 *
 * Pelaaja ei voi kayttaa esineita, joiden tyyppi on Tavara. Tama tuskin tulee muuttumaan.
 * 
 * Created by Miquel on 10.2.2016.
 *
 * Upgraded by Jyha since 17.2.2016.
 */


public class Tavara implements Serializable{
    protected int tavara_Laatu;  //Laatu kulkee nollasta kymmeneen, kun pelissa on 11 tasoa. Johonkin tarvitaan array, joka maarittaa kullekin numerolle oikean laadun.
    protected String tavara_Nimi;
    protected int tavara_Hinta;

    protected int tavara_Elama;  //Mahdollistaa sen, etta esineet antavat ylimaaraisia elamapisteita.
    protected int tavara_Voima;
    protected int tavara_Suoja;
    protected double tavara_Tarkkuus;
    protected double tavara_CritRate;

    //Metodi, jolla voi luoda oletustavaran. alkaa kayttaka tata konstruktoria.
    public Tavara(){
        this.tavara_Laatu = 0;
        this.tavara_Nimi = "NO NAME NO NAME";
        this.tavara_Hinta = 0;
        this.tavara_Elama = 0;
        this.tavara_Voima = 0;
        this.tavara_Suoja = 0;
        this.tavara_Tarkkuus = 0.0;
        this.tavara_CritRate = 0.00;
    }

    /**
     * Luo tavaran annettujen tietojen perusteella.
     * @param laatu  Tavaran laatu. Laatu maarittelee tason, jolla esine ilmestyy.
     * @param nimi  Tavaran nimi.
     * @param hinta  Tavaran ostohinta.
     * @param elama  Tavaran antamien elamapisteiden maara (useimmiten nolla).
     * @param voima  Tavaran antama voima.
     * @param suoja  Tavaran antama suoja.
     * @param tarkkuus  Tavaran antama tarkkuus.
     * @param CritRate  Tavaran antama kritikaalisen osuman mahdollisuus.
     */
    public Tavara(int laatu, String nimi, int hinta, int elama, int voima, int suoja, double tarkkuus, double CritRate) {
        this.tavara_Laatu = laatu;
        this.tavara_Nimi = nimi;

        //Tavaralle annettujen arvojen taytyy olla >= 0.
        if(hinta >= 0 && elama >= 0 && voima >= 0 && suoja >= 0 && tarkkuus >= 0.0 && CritRate >= 0.00) {
            this.tavara_Hinta = hinta;
            this.tavara_Elama = elama;
            this.tavara_Voima = voima;
            this.tavara_Suoja = suoja;
            this.tavara_Tarkkuus = tarkkuus;
            this.tavara_CritRate = CritRate;
        } else {
            System.out.println("Tavaralle annettujen arvojen täytyy olla >= 0!");
            this.tavara_Hinta = 0;
            this.tavara_Elama = 0;
            this.tavara_Voima = 0;
            this.tavara_Suoja = 0;
            this.tavara_Tarkkuus = 0.0;
            this.tavara_CritRate = 0.00;
        }
    }

    //Havainnointimetodit kullekin tavaran ominaisuudelle.
    /**
     * @return Palauttaa tavaran laadun numerona.
     */
    public int annaLaatu() {
        return tavara_Laatu;
    }
    /**
     * @return Palauttaa tavaran nimen.
     */
    public String annaNimi(){
        return tavara_Nimi;
    }
    /**
     * @return Palauttaa tavaran hinnan.
     */
    public int annaHinta() {
        return tavara_Hinta;
    }
    /**
     * @return Palauttaa tavaran antaman elaman maaran.
     */
    public int tavara_Elama() {
        return tavara_Elama;
    }
    /**
     * @return Palauttaa tavaran antaman voiman maaran.
     */
    public int annaVoima(){
        return tavara_Voima;
    }
    /**
     * @return Palauttaa tavaran antaman suojan maaran.
     */
    public int annaSuoja() {
        return tavara_Suoja;
    }
    /**
     * @return Palauttaa tavaran antaman tarkkuuden maaran.
     */
    public double annaTarkkuus() {
        return tavara_Tarkkuus;
    }
    /**
     * @return Palauttaa tavaran antaman kritikaalisen osuman mahdollisuuden.
     */
    public double annaCritRate() {
        return tavara_CritRate;
    }

    /**
     * Palauttaa minka tahansa esineen tiedot luettavassa muodossa.
     * Metodi ei ole yhta hienostunut kuin luokissa Ase ja Panssari.
     */
    public String toString(){
        return this.tavara_Nimi + " Laatu: " + this.tavara_Laatu + " Elämä: " + this.tavara_Elama + " Voima: " + this.tavara_Voima + " Suoja: " + this.tavara_Suoja + " Tarkkuus: " + this.tavara_Tarkkuus + " Kritikaalisuus: " + this.tavara_CritRate;
    }
}
