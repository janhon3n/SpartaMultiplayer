package Client;

import Server.ClientData;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Miquel on 11.1.2017.
 */
public interface ClientServerInterface extends Remote {

    public String introduce(String s) throws RemoteException;
    public ResultData makeAnAction(SpartaAction spartaAction) throws RemoteException;
}
