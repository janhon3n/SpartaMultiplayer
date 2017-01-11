package Server;

import java.io.Serializable;

/**
 * Created by Miquel on 14.12.2016.
 */
public class ClientData implements Serializable{

    private final int id;
    private final String ip;

    public ClientData(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public String getIp(){
        return ip;
    }
    public int getId(){
        return id;
    }

    public boolean isSame(ClientData cd){
        if(this.id == cd.id && this.ip.equals(cd.ip)){
            return true;
        }
        return false;
    }
}
