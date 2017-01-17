package Game;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Miquel on 23.2.2016.
 */
public class VastustajaTehdas {

    private String tiedostoPolku = "xml/vastustajat.xml";

    public Vastustaja annaVastustaja(int edistyminen) throws IllegalArgumentException {
        if(edistyminen > 10) throw new IllegalArgumentException("Edistyminen liian suuri");
        return luoVastustaja(edistyminen);
    }

    public void listaa(){
        for(Vastustaja v : luoVastustajat()){
            System.out.println("\n"+v);
        }
    }

    private ArrayList<Vastustaja> luoVastustajat() {

        ArrayList<Vastustaja> lista = new ArrayList<Vastustaja>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new File(tiedostoPolku));
            Element juuri = doc.getDocumentElement();
            NodeList vastustajaNodet = juuri.getElementsByTagName("vastustaja");
            for (int i = 0; i < vastustajaNodet.getLength(); i++) {

                if (vastustajaNodet.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    String nimi = "";
                    int elama = 0, voima = 0, suoja = 0, edistyminen = 0, xp = 0, raha = 0;
                    double tarkkuus = 0, critti = 0;

                    Element e = (Element) vastustajaNodet.item(i);
                    NodeList ominaisuusNodet = e.getChildNodes();
                    for (int o = 0; o < ominaisuusNodet.getLength(); o++) {
                        if (ominaisuusNodet.item(o).getNodeType() == Node.ELEMENT_NODE) {
                            switch (ominaisuusNodet.item(o).getNodeName()) {
                                case "nimi":
                                    nimi = ominaisuusNodet.item(o).getTextContent();
                                    break;
                                case "elama":
                                    elama = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "voima":
                                    voima = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "suoja":
                                    suoja = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "tarkkuus":
                                    tarkkuus = Double.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "critti":
                                    critti = Double.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "edistyminen":
                                    edistyminen = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "xp":
                                    xp = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "raha":
                                    raha = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                            }
                        }
                    }
                    lista.add(new Vastustaja(nimi, elama, voima, suoja, tarkkuus, critti, xp, raha));
                }
            }

        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            return lista;

        }
    }

    private Vastustaja luoVastustaja(int edis) {

        Vastustaja v = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new File(tiedostoPolku));
            Element juuri = doc.getDocumentElement();
            NodeList vastustajaNodet = juuri.getElementsByTagName("vastustaja" + edis);
            for (int i = 0; i < vastustajaNodet.getLength(); i++) {

                if (vastustajaNodet.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    String nimi = "";
                    int elama = 0, voima = 0, suoja = 0, edistyminen = 0, xp = 0, raha = 0;
                    double tarkkuus = 0, critti = 0;

                    Element e = (Element) vastustajaNodet.item(i);
                    NodeList ominaisuusNodet = e.getChildNodes();
                    for (int o = 0; o < ominaisuusNodet.getLength(); o++) {
                        if (ominaisuusNodet.item(o).getNodeType() == Node.ELEMENT_NODE) {
                            switch (ominaisuusNodet.item(o).getNodeName()) {
                                case "nimi":
                                    nimi = ominaisuusNodet.item(o).getTextContent();
                                    break;
                                case "elama":
                                    elama = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "voima":
                                    voima = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "suoja":
                                    suoja = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "tarkkuus":
                                    tarkkuus = Double.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "critti":
                                    critti = Double.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "edistyminen":
                                    edistyminen = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "xp":
                                    xp = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "raha":
                                    raha = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                            }
                        }
                    }
                    v = new Vastustaja(nimi, elama, voima, suoja, tarkkuus, critti, xp, raha);
                }
            }

        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            return v;
        }
    }
}
