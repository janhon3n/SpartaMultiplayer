package Game;

import java.util.Scanner;

public class PaaValikko extends Valikko {

    private Peli peli;

    public PaaValikko(Peli peli, Scanner sc) {
        super(sc);
        this.peli = peli;

        this.lisaaVaihtoehto(new Vaihtoehto("Tulosta sankarin tiedot") {
            @Override
            public void suorita() {
                System.out.println("\n" + peli.annaPelaaja());
            }
        });
        //N�m� vaihtoehdot kaatavat pelin, jos kutsutaan Erkki Kailan p�ihitt�misen j�lkeen.
        if(Peli.edistyminen < 10) {
        	this.lisaaVaihtoehto(new Vaihtoehto("Näytä seuraavan vastustajan tiedot") {
        		@Override
        		public void suorita() {
        			System.out.println();
        			System.out.println(peli.annaVt().annaVastustaja(peli.annaEdistyminen()).toString());
        		}
        	});
        	this.lisaaVaihtoehto(new Vaihtoehto("Taistele") {
            	@Override
            	public void suorita() {
                	peli.uusiTaistelu();
            	}
        	});
        }
        this.lisaaVaihtoehto(new Vaihtoehto("Kauppaan") {
            @Override
            public void suorita() {
                peli.siirryKauppaan();
            }
        });
        this.lisaaVaihtoehto(new Vaihtoehto("Pelaa moninpeliä"){
            @Override
            public void suorita() {
                peli.aloitaMoninpeli();
            }
        });
        this.lisaaVaihtoehto(new Vaihtoehto("Tallenna ja lopeta") {
            @Override
            public void suorita() {
                peli.annaAloitus().tallennaPeli(peli);
                peli.exit();
            }
        });
        this.lisaaVaihtoehto(new Vaihtoehto("Lopeta tallentamatta") {
            @Override
            public void suorita() {
                peli.exit();
            }
        });
    }

    @Override
    public void tulosta() {
        System.out.println();
        System.out.println("Taso " + peli.annaEdistyminen());
        super.tulosta();
    }
}