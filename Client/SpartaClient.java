package Client;

import Server.ClientData;
import Server.ServerInterface;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Miquel on 14.12.2016.
 */
public class SpartaClient extends UnicastRemoteObject implements ClientServerInterface {

    ClientData ownClientData;
    ClientData opponentClientData;
    ServerInterface spartaServer;
    boolean myTurn;
    boolean closeGame = false;

    public SpartaClient() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException, UnknownHostException {
        spartaServer = (ServerInterface) Naming.lookup("//localhost/spartaServer");
        String ip = Inet4Address.getLocalHost().getHostAddress();
        int id = spartaServer.getId();
        ownClientData = new ClientData(id, ip);
        opponentClientData = getMatchFromServer();
    }

    private ClientData getMatchFromServer() throws RemoteException, InterruptedException {
        ClientData ocd = spartaServer.findGame(ownClientData);
        while (ocd == null) {
            Thread.sleep(1000);
            ocd = spartaServer.findGame(ownClientData);
        }
        System.out.println("Game found at: " + ocd.getIp() + ":" + ocd.getId());
        return ocd;
    }

    public void createConnectionToOpponent() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        System.out.println("Creating RMI ClientServer");

        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }

        // Bind this object instance to the name "RmiServer"
        Naming.rebind("//localhost/spartaClientServer" + ownClientData.getId(), this);
        System.out.println("ClientServer created and available");
        Thread.sleep(1200);


        String opponentAddress = "//" + opponentClientData.getIp() + "/spartaClientServer" + opponentClientData.getId();
        System.out.println("Trying to establish connection to opponent at " + opponentAddress);
        ClientServerInterface opponentClientServer = (ClientServerInterface) Naming.lookup(opponentAddress);


        if (ownClientData.getId() > opponentClientData.getId()) {
            myTurn = true;
            Thread.sleep(1200);
            System.out.println("Making first contact to opponent");
            System.out.println("OPPONENT: "+opponentClientServer.testConnection("Is this working?"));
        } else {
            myTurn = false;
            System.out.println("Waiting for opponent to make contact");
        }

        while(!closeGame){
            if (myTurn){
                //ask user input
                //send data to
            }
        }
    }


    @Override
    public String testConnection(String s) {
        System.out.println("OPPONENT: "+s);
        myTurn = true;
        return "Succefull";
    }

    public static void main(String args[]) {
        SpartaClient spartaClient;
        try {
            spartaClient = new SpartaClient();
        } catch (Exception e) {
            System.out.println("Program is not working right. Pls fix.");
            e.printStackTrace();
            return;
        }

        try {
            spartaClient.createConnectionToOpponent();
        } catch (RemoteException re) {
            re.printStackTrace();
            //yritä uudestaan 5 kertaa ja palaa valikkoon
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            //palaa valikkoon
        } catch (NotBoundException nbe) {
            nbe.printStackTrace();
            //palaa valikkoon
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}