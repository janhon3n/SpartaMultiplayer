package Client;

import java.io.Serializable;

/**
 * Created by Miquel on 11.1.2017.
 */
public class SpartaAction implements Serializable {

    private int typeOfAction;
    private String message;

    public SpartaAction(){
    }
    public SpartaAction(int toa, String msg){
        this.message = msg;
        this.typeOfAction = toa;
    }

    public void setTypeOfAction(int toa){
        this.typeOfAction = toa;
    }
    public int getTypeOfAction(){
        return typeOfAction;
    }

    public void setMessage(String msg){
        this.message = msg;
    }
    public String getMessage(){
        return this.message;
    }
}
