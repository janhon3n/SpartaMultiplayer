package Game;



public class Taistelu {

    Hahmo t1, t2;

    public Taistelu(Hahmo t1, Hahmo t2){
        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean taistele(){
        //taistelun koodi tahan
        //taistelumekaniikka toimii niin, etta pelaaja ja vastustaja iskevat toisiaan vuorotellen,
        //kunnes jomman kumman elamapisteet saavuttavat (tai alittavat) arvon 0.

        int t1elama = t1.annaElama();
        int t2elama = t2.annaElama();

        while(true){
            iske(t1,t2);
            Peli.tauko(1000);
            if(!t2.elossa()){
                System.out.println(t2.annaNimi() + " kuoli taistelussa.\n");
                Peli.tauko(1500);
                return true;
            } else {
                if(t2.annaElama() < t2elama / 2){
                    t2elama = 0;
                    System.out.println(t2.annaNimi() + " ei voi hyvin. Hänella on enää puolet elämästään jäljellä.");
                    Peli.tauko(1500);
                }
            }
            iske(t2,t1);
            Peli.tauko(1000);
            if(!t1.elossa()){
                System.out.println(t1.annaNimi() + " kuoli taistelussa.\n");
                Peli.tauko(1500);
                return false;
            } else {
                if(t1.annaElama() < t1elama / 2){
                    t1elama = 0;
                    System.out.println(t1.annaNimi() + " ei voi hyvin. Hänellä on enää puolet elämästään jäljellä.");
                    Peli.tauko(1500);
                }
            }
        }
    }


    //Seuraava metodi saa parametrikseen hyakkaajan ja puolustajan ja laskee naiden ominaisuuksien perusteella aiheutettavan vahingon.
    //Puolustajan elamapisteita vahennetaan vahingon verran ja vahingon maara palautetaan tulostettavaksi

    /**
     *
     * @param hyakkaaja
     * @param puolustaja
     * @return palauttaa aiheutetun vahingon maaran (int)
     */
    private int iske(Hahmo hyakkaaja, Hahmo puolustaja) {


        if (Math.random() < hyakkaaja.annaCritRate()) { //jos critical hit

            double suhdeluku = (((4*hyakkaaja.annaTarkkuus())+4)/((puolustaja.annaSuoja()/100.0)*40));
            double hitti = (suhdeluku/10)*hyakkaaja.annaVoima();
            double crithitti = (0.5*hitti)+(Math.random()*(1.5*hitti));
            puolustaja.hahmo_Elama = puolustaja.hahmo_Elama - (int)crithitti;

            int vahinko = (int) crithitti;
            
            if(vahinko <= 0){
                System.out.println(hyakkaaja.hahmo_Nimi + " osui harhaan");
            } else {
            	System.out.println(hyakkaaja.hahmo_Nimi + " iskee viholliseen "+puolustaja.hahmo_Nimi + " kriittistä vahinkoa: "+vahinko+"!");
            }
            	return vahinko;
            	
            


        }else{  //suhdeluku, hyakkaajan tarkkuudesta ja puolustajan suojan valisesta suhteesta laskettu arvo
            //hitti = vahinko, joka vahennetaan hahmon elamapisteista. Se muodostuu vastustajan voimatasosta, joka kerrotaan suhdeluvulla, mukana Random-olio tuomaan variaatiota.

            double suhdeluku = (((4*hyakkaaja.annaTarkkuus())+4)/((puolustaja.annaSuoja()/100.0)*40));
            double hitti = (suhdeluku/10)*Math.random()*hyakkaaja.annaVoima();
            puolustaja.hahmo_Elama = puolustaja.hahmo_Elama - (int)hitti;

            int vahinko = (int) hitti;

            if(vahinko <= 0){
                System.out.println(hyakkaaja.hahmo_Nimi + " osui harhaan.");
            } else {
                System.out.println(hyakkaaja.hahmo_Nimi+" iskee vihollista " +puolustaja.hahmo_Nimi + " aiheuttaen vahinkoa: "+ vahinko);
            }

            return (int)hitti;
        }


    }
}