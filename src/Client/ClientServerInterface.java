package Client;

import Game.Hahmo;
import Server.ClientData;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Miquel on 11.1.2017.
 */
public interface ClientServerInterface extends Remote {

    public void introduce(String nimi, int taso, String msg) throws RemoteException;
    public ResultData makeAnAction(SpartaAction spartaAction) throws RemoteException;
    public void updateStats(Hahmo hahmo) throws RemoteException;
}
