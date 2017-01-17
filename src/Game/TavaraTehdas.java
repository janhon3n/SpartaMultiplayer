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
 * Created by Miquel on 10.2.2016.
 */
public class TavaraTehdas {

    private String tiedostoPolku = "xml/tavarat.xml";

    public ArrayList<Tavara> luoTavarat() {
        ArrayList<Tavara> lista = new ArrayList<Tavara>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            ArrayList<Ase> aseet = new ArrayList<Ase>();
            ArrayList<Panssari> panssarit = new ArrayList<Panssari>();

            Document doc = docBuilder.parse(new File(tiedostoPolku));
            Element juuri = doc.getDocumentElement();
            NodeList aseNodet = juuri.getElementsByTagName("ase");
            for (int i = 0; i < aseNodet.getLength(); i++) {

                if (aseNodet.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    int laatu = 0, hinta = 10, elama = 0, voima = 0, suoja = 0, tyyppi = 0;
                    String nimi = "";
                    double tarkkuus = 0, critti = 0;

                    Element e = (Element) aseNodet.item(i);
                    NodeList ominaisuusNodet = e.getChildNodes();
                    for (int o = 0; o < ominaisuusNodet.getLength(); o++) {
                        if (ominaisuusNodet.item(o).getNodeType() == Node.ELEMENT_NODE) {
                            switch (ominaisuusNodet.item(o).getNodeName()) {
                                case "laatu":
                                    laatu = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "nimi":
                                    nimi = ominaisuusNodet.item(o).getTextContent();
                                    break;
                                case "hinta":
                                    hinta = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
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
                                case "tyyppi":
                                    tyyppi = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                            }
                        }
                    }
                    aseet.add(new Ase(laatu, nimi, hinta, elama, voima, suoja, tarkkuus, critti, tyyppi));
                }
            }

            NodeList panssariNodet = juuri.getElementsByTagName("panssari");
            for (int i = 0; i < panssariNodet.getLength(); i++) {

                if (panssariNodet.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    int laatu = 0, hinta = 10, elama = 0, voima = 0, suoja = 0, tyyppi = 0;
                    String nimi = "";
                    double tarkkuus = 0, critti = 0;

                    Element e = (Element) panssariNodet.item(i);
                    NodeList ominaisuusNodet = e.getChildNodes();
                    for (int o = 0; o < ominaisuusNodet.getLength(); o++) {
                        if (ominaisuusNodet.item(o).getNodeType() == Node.ELEMENT_NODE) {
                            switch (ominaisuusNodet.item(o).getNodeName()) {
                                case "laatu":
                                    laatu = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                                case "nimi":
                                    nimi = ominaisuusNodet.item(o).getTextContent();
                                    break;
                                case "hinta":
                                    hinta = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
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
                                case "tyyppi":
                                    tyyppi = Integer.valueOf(ominaisuusNodet.item(o).getTextContent());
                                    break;
                            }
                        }
                    }
                    panssarit.add(new Panssari(laatu, nimi, hinta, elama, voima, suoja, tarkkuus, critti, tyyppi));
                }
            }
            lista.addAll(aseet);
            lista.addAll(panssarit);

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
}