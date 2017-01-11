package Server;

        import java.rmi.Remote;
        import java.rmi.RemoteException;

/**
 * Created by Miquel on 14.12.2016.
 */
public interface ServerInterface extends Remote {
    public int getId() throws RemoteException;
    public ClientData findGame(ClientData ip) throws RemoteException;
}

