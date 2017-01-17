package Client;

import Server.ClientData;
import Server.ServerInterface;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Miquel on 14.12.2016.
 */
public class SpartaClient extends UnicastRemoteObject implements ClientServerInterface {

    private Scanner sc;
    ClientData ownClientData;
    ClientData opponentClientData;
    ServerInterface spartaServer;
    boolean myTurn;
    boolean closeGame = false;
    Registry serverRmiRegistry;
    boolean interruptGameSearch = false;

    public SpartaClient(Scanner sc) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException, UnknownHostException {
        this.sc = sc;
        System.out.println("Connecting to server...");
        serverRmiRegistry = LocateRegistry.getRegistry("192.168.100.40", 1099);

        spartaServer = (ServerInterface) serverRmiRegistry.lookup("spartaServer");
        String ip = Inet4Address.getLocalHost().getHostAddress();
        int id = spartaServer.getId();
        ownClientData = new ClientData(id, ip);
        opponentClientData = getMatchFromServer();
        System.out.println("I am at " + ownClientData.getIp() + ":" + ownClientData.getId());
    }

    private ClientData getMatchFromServer() throws RemoteException, InterruptedException {
        System.out.println("Looking for an opponent...");

        InterruptGameSearch igs = new InterruptGameSearch(sc, this);
        igs.start();
        ClientData ocd = spartaServer.findGame(ownClientData);
        while (ocd == null) {
            Thread.sleep(1000);
            ocd = spartaServer.findGame(ownClientData);
            if(interruptGameSearch && ocd == null){
                spartaServer.removeClient(ownClientData);
                throw new InterruptedException("User interrupted game search");
            }
        }
        System.out.println("Opponent found at: " + ocd.getIp() + ":" + ocd.getId());
        return ocd;
    }

    public void createConnectionToOpponent() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException, AlreadyBoundException {
        System.out.println("Creating RMI ClientServer");

        // Create RMI registry to local machine
        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }
        Registry localRmiRegistry = LocateRegistry.getRegistry("localhost");

        // Bind this object instance to the name "RmiServer"
        localRmiRegistry.bind("spartaClientServer"+ownClientData.getId(), this);
        System.out.println("ClientServer created and added to registry");
        Thread.sleep(1200);

        System.out.println("Trying to access opponents RMI object at the registry");
        ClientServerInterface opponentClientServer = (ClientServerInterface) Naming.lookup("//" + opponentClientData.getIp() + "/spartaClientServer"+opponentClientData.getId());

        if (ownClientData.getId() > opponentClientData.getId()) {
            myTurn = true;
            Thread.sleep(1200);
            System.out.println("Making first contact to opponent");
            String introduceMsg = "Hello! I am a sparta client";
            System.out.println("ME: "+introduceMsg);
            System.out.println("OPPONENT: "+opponentClientServer.introduce(introduceMsg));
        } else {
            myTurn = false;
            System.out.println("Waiting for opponent to make contact");
        }


        /*  THE GAME LOOP */
        while(!closeGame){
            if(myTurn){
                System.out.println("My turn!");
                Thread.sleep(200);
                //ask user input
                System.out.print("Send message to opponent: ");
                String msg =  "testi";
                //create SpartaAction object to hold data
                SpartaAction sa = new SpartaAction(0, msg);
                //send object to opponent
                opponentClientServer.makeAnAction(sa);
                myTurn = false;
                System.out.println("Opponents turn.");
            }
            Thread.sleep(100);
        }
    }


    /* RMI Methods */
    @Override
    public String introduce(String s) {
        System.out.println("OPPONENT: "+s);
        String introduceMsg = "Well hello! Nice to meet you";
        System.out.println("ME: " + introduceMsg);
        return introduceMsg;
    }
    @Override
    public ResultData makeAnAction(SpartaAction spartaAction) throws RemoteException {
        System.out.println("Opponent makes an action: "+spartaAction.getTypeOfAction());
        System.out.println("OPPONENT: "+spartaAction.getMessage());
        this.myTurn = true;
        return null;
    }

    public void interruptGameSearch(){
        this.interruptGameSearch = true;
    }
}