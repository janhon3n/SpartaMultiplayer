package Game;

public class ValikkoPoikkeus extends Exception {

	public ValikkoPoikkeus(String viesti){
		super(viesti);
	}
	public ValikkoPoikkeus(){
		super();
	}
}