package modele;

import java.util.ArrayList;
import java.util.List;

import application.DessinVoiture;

public class Voiture {
	private int id; // Identifiant de la voiture
	int x; // Coordonnee x de la voiture
	int y; // Coordonnee y de la voiture
	private List<Noeud> trajet; // Liste des noeuds du trajet prevu
	private List<Noeud> routeRestante; // Liste des restant a parcourir
	private Noeud origine; // Noeud de depart
	private Noeud destination; // Noeud de destination finale
	private boolean pause; // Pause pendant le parcours
	private boolean arrivee; // Indique si la voiture est arrivee
	private boolean accidente;
	private DessinVoiture dv;


	public Voiture(){}

	// Construit une voiture
	public Voiture(int id){
		this.id = id;
		trajet = new ArrayList<>();
		routeRestante = new ArrayList<>();
	}

	/**construit une voiture
	 * @param id identifiant de la voiture
	 * @param origine noeud de depart
	 * @param destination noeud d'arrivee
	 * */
	public Voiture(int no, Noeud origine, Noeud destination) {
		this(no);
		this.origine = origine;
		x = origine.x;
		y = origine.y;
		this.destination = destination;
		calculerRoute();
		for(Noeud n:trajet)routeRestante.add(n);
	}

	/**construit une voiture
	 * @param id identifiant de la voiture
	 * @param xo yo coordonnees du noeud d'origine
	 * @param xd yd coordonnees du noeud de destinatin
	 * */
	public Voiture(int no, int xo, int yo, int xd, int yd) {
		this(no);
		this.origine = ReseauRoutier.getNoeud(xo, yo);
		x = origine.x;
		y = origine.y;
		this.destination = ReseauRoutier.getNoeud(xd, yd);
		calculerRoute();
		for(Noeud n:trajet)routeRestante.add(n);
	}
	
	public Voiture(int no, int[] o, int[] d) {
		this(no);
		this.origine = ReseauRoutier.getNoeud(o[0], o[1]);
		x = origine.x;
		y = origine.y;
		this.destination = ReseauRoutier.getNoeud(d[0], d[1]);
		calculerRoute();
		for(Noeud n:trajet)routeRestante.add(n);
	}

	// Calcul la route entre origine et destination (suppose que ces points soient aient une meme abscisse ou ordonnee)
	public void calculerRoute() {
		boolean enLigne = (origine.getY()==destination.getY());
		if (enLigne) {
			int ligne = origine.getY();
			int sens = destination.getX() - origine.getX();
			sens = sens/Math.abs(sens);
			for(int i=origine.getX(); i!=destination.getX(); i+=sens) {
				Noeud n = ReseauRoutier.getNoeud(i, ligne);
				trajet.add(n);
			}
		}
		else {
			int colonne = origine.getX();
			int sens = destination.getY() - origine.getY();
			sens = sens/Math.abs(sens);
			for(int i=origine.getY(); i!=destination.getY(); i+=sens) {
				Noeud n = ReseauRoutier.getNoeud(colonne, i);
				trajet.add(n);
			}
		}
		trajet.add(destination);
	}

	// Retourne le prochain noeud de la route (depile routeRestante)
	public Noeud prochainNoeud() {
		Noeud suivant = null;
		if(!routeRestante.isEmpty()) {
			suivant = routeRestante.remove(0);
		}
		return suivant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Voiture other = (Voiture) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<Noeud> getRouteRestante() {
		return this.routeRestante;
	}
	// Affecte les coordonnees x,y de la voiture
	public void setXY(int x, int y)  { 
		this.x = x; this.y = y; 
		if (destination!=null && x==destination.x && y==destination.y) arrivee = true;
	}

	// Retourne une chaine de caracteres contenant l'identfiant de la voiture et son chemin prevu
	public String toString() {
		StringBuilder sb = new StringBuilder("voiture ").append(id);
		sb.append(". chemin = ");
		for(Noeud n:trajet) sb.append(n).append(" - ");
		return sb.toString();      
	}

	public int getId() {
		return this.id;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isPause() {
		return pause;
	}
	public boolean isArrivee() {
		return arrivee;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public DessinVoiture getDv() {
		return dv;
	}

	public void setDv(DessinVoiture dv) {
		this.dv = dv;
	}

	public boolean isAccidente() {
		return accidente;
	}

	public void setAccidente(boolean accidente) {
		this.accidente = accidente;
	}
}
