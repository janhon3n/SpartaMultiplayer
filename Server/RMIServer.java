package Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Miquel on 14.12.2016.
 */
public class RMIServer extends UnicastRemoteObject implements ServerInterface {

    ArrayList<Match> matches = new ArrayList<Match>();
    int nextId = 0;


    public RMIServer() throws RemoteException {
        super(0);
    }


    @Override
    public int getId(){
        return nextId++;
    }

    @Override
    public ClientData findGame(ClientData cd) throws RemoteException {
        for(Match m : matches){
            if(m.has2()) {
                System.out.println(m.getKey().getIp() + ":" + m.getKey().getId() + " - " + m.get2().getIp() + ":" + m.get2().getId());
            } else {
                System.out.println(m.getKey().getIp() + ":" + m.getKey().getId() + " - null");
            }
            System.out.println();
        }

        for(Match m : matches){
            if(m.getKey().isSame(cd)){
                if(m.has2()){
                    return m.get2();
                } else {
                    return null;
                }
            }
        }

        for(Match m : matches){
            if(!m.has2()){
                m.set2(cd);
                return m.getKey();
            }
        }

        Match m = new Match(cd);
        matches.add(m);
        return null;
    }


    public static void main(String args[]) throws Exception {
        System.out.println("RMI server started");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        //Instantiate RmiServer

        RMIServer rmiServer = new RMIServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/spartaServer", rmiServer);
        System.out.println("RmiServer created and available");
    }


}
