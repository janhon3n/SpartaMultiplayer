package Game;

public abstract class Vaihtoehto {

	private String teksti;

	public abstract void suorita();
	
	public Vaihtoehto(String teksti){
		this.teksti = teksti;
	}
	
	@Override
	public String toString(){
		return teksti;
	}
}