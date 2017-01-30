package Game;

// Versio 2.125!
/**
 * 
 *@author Juho
 *Luokka Hahmo, jossa aliluokkina Pelaaja seka Vastustaja Viimeista Spartaa varten
 *
 *Luokka mallintaa yhta pelin hahmoa, jolla on useita ominaisuuksia.
 *Nama ominaisuudet erikoistuvat syvemmalle luokissa Pelaaja ja Vastustaja.
 */


import java.io.Serializable;

//Tama on yliluokka, luokka luo hahmon. Hahmo voi olla pelaaja tai vastustaja.
public class Hahmo implements Serializable {
	//Pelaaja seka viholliset jakavat kaikki seuraavat ominaisuudet:

	protected String hahmo_Nimi;
	protected int hahmo_Elama;  //Maarittaa hahmon elamapisteet
	protected int hahmo_Voima;  //Yksinkertaisesti kasvattaa maksimidamagea
	protected int hahmo_Suoja;  //Palauttaa elamaa joka vuorolla
	protected double hahmo_Tarkkuus;  //Tarkkuus lisaa keskimaaraista damagea
	protected double hahmo_CritRate;  //Crit Rate lisaa kritikaalisien osumien mahdollisuutta
	
	

	//Luo hahmon, jolla on oletusstatsit ja jonka nimea ei ole asetettu
	//Tata metodia ei koskaan tulisi kayttaa hahmojen luomisessa!!
	public Hahmo() {
		this.hahmo_Nimi = "NO NAME NO NAME";
		this.hahmo_Elama = 100;
		this.hahmo_Voima = 10;
		this.hahmo_Suoja = 10;
		this.hahmo_Tarkkuus = 10.0;
		this.hahmo_CritRate = 0.10;
	}

	/**
	 * 
	 * @param nimi Hahmon nimi.
	 * @param elama Hahmon nykyinen elaman maara, laskee taisteluiden aikana.
	 * @param voima Voima kasvattaa hahmon tekemaa maksimaalista vahinkoa.
	 * @param suoja Suoja vahentaa vihollisen iskujen tekemaa vahinkoa.
	 * @param tarkkuus Tarkkuus lisaa hahmon iskujen keskimaaraista suuruutta.
	 * @param critrate Kritikaalisen osuman mahdollisuus.
	 * Jokaisella pelin hahmolla on nama ominaisuudet.
	 */
	//Luo hahmon, jonka jokainen statsi on maaritelty jo luontivaiheessa
	//Tama on metodi, jota tulisi superin avulla kutsua kaikkia hahmoja luotaessa!
	public Hahmo(String nimi, int elama, int voima, int suoja, double tarkkuus, double critrate) {
		this.hahmo_Nimi = nimi;  //Nimea ei tutkita ollenkaan, se voi olla mika tahansa
		if(elama > 0 && voima >= 0 && suoja >= 0 && tarkkuus >= 0 && critrate >= 0) {  //Statsit >= 0!
			this.hahmo_Elama = elama;
			this.hahmo_Voima = voima;
			this.hahmo_Suoja = suoja;
			this.hahmo_Tarkkuus = tarkkuus;
			this.hahmo_CritRate = critrate;  //Jos statsit >= 0, ne voidaan asettaa.
		} else {
			System.out.println("Hahmolle annettujen arvojen täytyy olla >= 0!");
			this.hahmo_Elama = 100;
			this.hahmo_Voima = 10;
			this.hahmo_Suoja = 10;
			this.hahmo_Tarkkuus = 10.0;
			this.hahmo_CritRate = 0.10;
		}
	}
	
	/**
	 * 
	 * @return Palauttaa true, jos hahmon elama on suurempi kuin 0. Muutoin palauttaa false.
	 */
	public boolean elossa() {
		if(hahmo_Elama <= 0) {
			return false;
		} else {
			return true;
		}}



	//Havainnointimetodit tietojen saamiseksi kullakin hahmolla:
	/**
	 * 
	 * @return Palauttaa hahmon nimen.
	 */
	public String annaNimi() {
		return hahmo_Nimi;  //Palauttaa nimen
	}
	/**
	 * 
	 * @return Palauttaa hahmon nykyisen elaman.
	 */
	public int annaElama() {
		return hahmo_Elama;  //Palauttaa elaman
	}
	/**
	 * 
	 * @return Palauttaa hahmon voiman.
	 */
	public int annaVoima() {
		return hahmo_Voima;  //Palauttaa voiman
	}
	/**
	 * 
	 * @return Palauttaa hahmon suojan.
	 */
	public int annaSuoja() {
		return hahmo_Suoja;  //Palauttaa suojan
	}
	/**
	 * 
	 * @return Palauttaa hahmon tarkkuuden.
	 */
	public double annaTarkkuus() {
		return hahmo_Tarkkuus;  //Palauttaa tarkkuuden
	}
	/**
	 * 
	 * @return Palauttaa hahmon kritikaalisen osuman mahdollisuuden.
	 */
	public double annaCritRate() {
		return hahmo_CritRate;  //Palauttaa kritikaalisuuden
	}
	//Ylla olevat metodit toimivat riippumatta siita, onko olio Hahmo, Pelaaja vai Vastustaja, silla kaikki jakavat nama samat tiedot.

	public void asetaElama(int elama){
		this.hahmo_Elama = elama;
	}
}






/**
 * @author Juho
 * Kasittelee vastustajaan liittyvia asioita.
 */
class Vastustaja extends Hahmo {
	private int vastustaja_XP;
	private int vastustaja_Raha;

	//Luo vastustajan oletusarvoilla
	//Tata metodia ei tulisi kayttaa hahmon luomiseen!!
	public Vastustaja() {
		super();
	}

	/**
	 * 
	 * @param nimi Vastustajan nimi.
	 * @param elama Vastustajan elamapisteet.
	 * @param voima Vastustajan voima.
	 * @param suoja Vastustajan suoja.
	 * @param tarkkuus Vastustajan tarkkuus.
	 * @param critrate Vastustajan mahdollisuus kritikaaliseen osumaan.
	 * @param xp Vastustajan pudottamien kokemuspisteiden maara (kun vastustajan voittaa).
	 * @param raha Vastustajan pudottaman rahan maara (kun vastustajan voittaa).
	 */
	public Vastustaja(String nimi, int elama, int voima, int suoja, double tarkkuus, double critrate, int xp, int raha) {
		this.hahmo_Nimi = nimi;
		if(elama >= 0 && voima >= 0 && suoja >= 0 && tarkkuus >= 0 && critrate >= 0) {  //Statsit >= 0!
			this.hahmo_Elama = elama;
			this.hahmo_Voima = voima;
			this.hahmo_Suoja = suoja;
			this.hahmo_Tarkkuus = tarkkuus;
			this.hahmo_CritRate = critrate;  //Jos statsit >= 0, ne voidaan asettaa.
			this.vastustaja_XP = xp;  //Maarittaa vihollisen droppaaman XP:n maaran
			this.vastustaja_Raha = raha;  //Maarittaa vihollisen droppaaman rahan maaran
		}
	}
	/**
	 * @return Palauttaa vastustajan antaman rahan maaran.
	 */
	public int annaRaha() {
		return this.vastustaja_Raha;
	}
	/**
	 * @return Palauttaa vastustajan antamien kokemuspisteiden maaran.
	 */
	public int annaXP() {
		return this.vastustaja_XP;
	}

	/**
	 * Tulostaa vastustajan tiedot luettavassa muodossa.
	 */
	public String toString() {
		return hahmo_Nimi + "n tiedot:\nElämä: " + hahmo_Elama + "\nVoima: " + hahmo_Voima + "\nSuoja: " + hahmo_Suoja + "\nTarkkuus: " + hahmo_Tarkkuus + "\nKritikaalisuus: " + hahmo_CritRate;
	}
}