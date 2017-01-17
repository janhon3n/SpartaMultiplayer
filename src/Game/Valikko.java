package Game;

import java.util.Scanner;
import java.util.ArrayList;

public class Valikko {

    private Scanner sc;
    private ArrayList<Vaihtoehto> vaihtoehdot = new ArrayList<Vaihtoehto>();

    public Valikko(Scanner sc) {
        this.sc = sc;
    }

    public void lisaaVaihtoehto(Vaihtoehto v) {
        this.vaihtoehdot.add(v);
    }

    public void tulosta() {
        for (int i = 0; i < vaihtoehdot.size(); i++) {
            System.out.println(i + 1 + ". " + vaihtoehdot.get(i));
        }
    }

    public void valitse() throws ValikkoPoikkeus {
        System.out.print("Valitse vaihtoehto: ");
        if(!sc.hasNextInt()){
            sc.next();
            throw new ValikkoPoikkeus("Syöttämäsi merkkijono ei ollut kokonaisluku");
        }
        int valinta = sc.nextInt();
        if (valinta < 1 || valinta > vaihtoehdot.size()) {
            throw new ValikkoPoikkeus("Vaihtoehtoa ei ollut valikossa");
        }
        vaihtoehdot.get(valinta - 1).suorita();
        System.out.println();
    }

    public void pakotaValinta() {
        boolean suoritettu = false;
        while (!suoritettu) {
            try {
                this.valitse();
                suoritettu = true;
            } catch (ValikkoPoikkeus vp) {
                System.out.println(vp.getMessage());
            }
        }
    }
}