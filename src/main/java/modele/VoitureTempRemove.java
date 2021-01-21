package modele;

public class VoitureTempRemove {
	private Voiture voiture;
	private int temps;
	
	
	public VoitureTempRemove(Voiture voiture) {
		super();
		this.voiture = voiture;
		this.temps = 0;
	}
	
	public void incrementerTemps() {
		this.setTemps(this.getTemps() + 1);
	}
	
	public Voiture getVoiture() {
		return voiture;
	}
	public void setVoiture(Voiture voiture) {
		this.voiture = voiture;
	}
	public int getTemps() {
		return temps;
	}
	public void setTemps(int temps) {
		this.temps = temps;
	}
	
	
}
