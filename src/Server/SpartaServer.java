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
            if(m.getKey().isSame(cd)){ // if client is key
                if(m.has2()){ // and match has second client
                    System.out.println("Match made: " + m.getKey().getIp() + ":" + m.getKey().getId() + " - " +cd.getIp() + ":" + cd.getId());
                    return m.get2(); // return matched clients data
                } else { // and match still waiting for second client
                    return null; // return null
                }
            }
        }

        // if client new
        for(Match m : matches){
            if(!m.has2()){ // and there is a match waiting for second client
                System.out.println("New client from "+cd.getIp() + ":" + cd.getId());
                m.set2(cd); // add connecting client to match
                return m.getKey(); // and return matches
            }
        }

        // if client new and no free matches present
        Match m = new Match(cd); // create a new match and set client to be the key
        System.out.println("New client from "+cd.getIp() + ":" + cd.getId());
        matches.add(m);
        return null;
    }

    public void removeClient(ClientData cd) throws RemoteException {
        for(Match m : matches){
            if(m.getKey().isSame(cd)){ // if client is key
                matches.remove(m);
            }
        }
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
