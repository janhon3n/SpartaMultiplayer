package Client;

import Game.Vaihtoehto;
import Game.Valikko;
import Game.ValikkoPoikkeus;
import Server.ClientData;
import Server.ServerInterface;

import java.util.Scanner;

/**
 * Created by Miquel on 17.1.2017.
 */
public class InterruptGameSearch extends Thread {

    SpartaClient spartaClient;
    Valikko v;
    private Scanner sc;

    public InterruptGameSearch(Scanner sc, SpartaClient spartaClient){
        this.spartaClient = spartaClient;
        this.sc = sc;
        v = new Valikko(sc);
        v.lisaaVaihtoehto(new Vaihtoehto("Keskeytä haku"){
            @Override
            public void suorita() {
                stopGameSearch();
            }
        });
    }

    public void run(){
        while(true) {
            try {
                v.tulosta();
                v.valitse();
                return;
            } catch (ValikkoPoikkeus vp) {
                System.out.println(vp.getMessage());
            }
        }
    }

    public void stopGameSearch(){
        spartaClient.interruptGameSearch();
    }
}
