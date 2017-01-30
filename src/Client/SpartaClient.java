package Client;

import Game.*;
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
    private ClientData ownClientData;
    private ClientData opponentClientData;
    private ServerInterface spartaServer;
    private boolean closeGame = false;
    private Registry serverRmiRegistry;
    private OnlineTaistelu onlineTaistelu;
    private Pelaaja pelaaja;
    private boolean userInterrupted = false;

    public SpartaClient(Scanner sc, Pelaaja pelaaja) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException, UnknownHostException {
        this.sc = sc;
        this.pelaaja = pelaaja;
        System.out.println("Connecting to server...");
        serverRmiRegistry = LocateRegistry.getRegistry("localhost", 1099);

        spartaServer = (ServerInterface) serverRmiRegistry.lookup("spartaServer");
        String ip = Inet4Address.getLocalHost().getHostAddress();
        int id = spartaServer.getId();
        ownClientData = new ClientData(id, ip);
        opponentClientData = getMatchFromServer();
        System.out.println("I am at " + ownClientData.getIp() + ":" + ownClientData.getId());
        System.out.println("Opponent found at: " + opponentClientData.getIp() + ":" + opponentClientData.getId());
    }

    private ClientData getMatchFromServer() throws RemoteException, InterruptedException {
        System.out.println("Looking for an opponent...");

        Valikko v = new Valikko(sc);
        v.lisaaVaihtoehto(new Vaihtoehto("Jatka hakua") {
            @Override
            public void suorita() {
            }
        });
        v.lisaaVaihtoehto(new Vaihtoehto("Keskeytä haku"){
            @Override
            public void suorita() {
                userInterruptedSearch();
            }
        });


        ClientData ocd = lookForOpponent(10);
        while(ocd == null){
            v.tulosta();
            v.pakotaValinta();
            if(userInterrupted){
                throw new InterruptedException("Käyttäjä keskeytti haun.");
            }
            ocd = lookForOpponent(10);
        }

        //----------> OCD varmasti !null

        return ocd;
    }

    private ClientData lookForOpponent(int times) throws RemoteException, InterruptedException {
        int counter = 0;
        ClientData ocd;
        while (counter < times) {
            ocd = spartaServer.findGame(ownClientData);
            Thread.sleep(1000);
            if (ocd != null) {
                return ocd;
            }
            counter++;
        }
        spartaServer.removeClient(ownClientData);
        return null;
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

        if(ownClientData.getId() > opponentClientData.getId()) {
            onlineTaistelu = new OnlineTaistelu(this, opponentClientServer, true, pelaaja, sc);
        } else {
            onlineTaistelu = new OnlineTaistelu(this, opponentClientServer, false, pelaaja, sc);
        }
        onlineTaistelu.startIntroduce();


        while(!(onlineTaistelu.opponentHasIntroduced() && onlineTaistelu.iHaveIntroduced())){
            Thread.sleep(600);
        }

        /*  THE GAME LOOP */
        while(!closeGame) {
            if(onlineTaistelu.checkTurn()){
                onlineTaistelu.executeTurn();
            }
            Thread.sleep(300);
        }
    }


    /* RMI Methods */
    @Override
    public void introduce(String nimi, int taso, String msg) {
        onlineTaistelu.opponentIntroduced();
        while(!onlineTaistelu.iHaveIntroduced()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Astut sisään areenalle. Kuulet ihmisten huutoja yleisöstä.");
        Peli.tauko(1500);
        System.out.println("Huomaat areenan toisessa päässä vastustajasi.");
        Peli.tauko(1500);
        System.out.println("Hän on " + nimi + ". Tason "+ taso + " taistelija");
        Peli.tauko(1500);
        System.out.println("Hän huomaa sinut ja huutaa: " + msg);
    }

    @Override
    public void updateStats(Hahmo hahmo) throws RemoteException {
        onlineTaistelu.updateStats(hahmo);
    }
    @Override
    public ResultData makeAnAction(SpartaAction spartaAction) throws RemoteException {
        System.out.println("Opponent makes an action: "+spartaAction.getTypeOfAction());

        ResultData resultData;
        switch(spartaAction.getTypeOfAction()){
            case 0: // lyönti
                onlineTaistelu.attack(spartaAction);
                break;
            case 1:
                break;
            case 2:
                onlineTaistelu.win(spartaAction);
                break;
            case 3:
                break;
            default:
                break;
        }

        onlineTaistelu.setTurn(true);
        return null;
    }

    public void userInterruptedSearch(){
        this.userInterrupted = true;
    }

    public void closeGame(){
        this.closeGame = true;
    }
}