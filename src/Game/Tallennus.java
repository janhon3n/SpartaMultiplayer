package Game;

import java.io.Serializable;

/**
 * Created by Miquel on 9.3.2016.
 */
public class Tallennus implements Serializable {

    Pelaaja pelaaja;
    int edistyminen;

    public Tallennus(Pelaaja pelaaja, int edistyminen){
        this.pelaaja = pelaaja;
        this.edistyminen = edistyminen;
    }

    public Pelaaja annaPelaaja(){
        return pelaaja;
    }
    public int annaEdistyminen(){
        return edistyminen;
    }
}
