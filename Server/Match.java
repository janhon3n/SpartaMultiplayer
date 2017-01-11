package Server;

import java.util.HashMap;

/**
 * Created by Miquel on 14.12.2016.
 */
/* nimestä huolimatta, tämä luokka ei mallinna tulitikkua, vaan jotain ihan muuta */
public class Match {

    ClientData keyClient;
    ClientData client2;


    public Match(ClientData key){
        keyClient = key;
    }

    public boolean has2(){
        if(client2 != null){
            return true;
        }
        return false;
    }

    public ClientData getKey(){
        return keyClient;
    }
    public ClientData get2(){
        return client2;
    }

    public void set2(ClientData ip){
        this.client2 = ip;
    }
}
