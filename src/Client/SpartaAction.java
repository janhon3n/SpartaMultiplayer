package Client;

import java.io.Serializable;

/**
 * Created by Miquel on 11.1.2017.
 */
public class SpartaAction implements Serializable {

    private int typeOfAction;
    private int damage;
    private boolean crithit;
    private String message;

    /* lyönti */
    public SpartaAction(int damage, boolean crithit, String message){
        this.typeOfAction = 0;
        this.damage = damage;
        this.crithit = crithit;
        this.message = message;
    }

    /* kuolema */
    public SpartaAction(String message){
        this.typeOfAction = 2;
        this.message = message;
    }

    public void setTypeOfAction(int toa){
        this.typeOfAction = toa;
    }
    public int getTypeOfAction(){
        return typeOfAction;
    }

    public String getMessage(){
        return message;
    }
    public double getDamage(){
        return damage;
    }
}
