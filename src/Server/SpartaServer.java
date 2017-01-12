package Server;

import javax.management.remote.rmi.RMIServer;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Miquel on 14.12.2016.
 */
public class SpartaServer extends UnicastRemoteObject implements ServerInterface {

    ArrayList<Match> matches = new ArrayList<Match>();
    int nextId = 0;


    public SpartaServer() throws RemoteException {
        super(1098);
    }


    @Override
    public int getId(){
        return nextId++;
    }

    @Override
    public ClientData findGame(ClientData cd) throws RemoteException {
        for(Match m : matches){
            if(m.getKey().isSame(cd)){
                if(m.has2()){
                    System.out.println("Match made: " + m.getKey().getIp() + ":" + m.getKey().getId() + " - " +cd.getIp() + ":" + cd.getIp());
                    return m.get2();
                } else {
                    return null;
                }
            }
        }

        for(Match m : matches){
            if(!m.has2()){
                System.out.println("New client from "+cd.getIp() + ":" + cd.getId());
                m.set2(cd);
                return m.getKey();
            }
        }

        Match m = new Match(cd);
        System.out.println("New client from "+cd.getIp() + ":" + cd.getId());
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

        SpartaServer spartaServer = new SpartaServer();

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/spartaServer", spartaServer);
        System.out.println("RmiServer created and available");
    }


}
