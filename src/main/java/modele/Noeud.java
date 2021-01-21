package modele;

import java.util.ArrayList;
import java.util.List;
import application.AppliTrafic;

public class Noeud extends Point{
    List<Noeud> noeudsAccessibles; // Liste des voisins accessibles
    List<Arc> arcEntrants; // Liste des arcs entrants
    List<Arc> arcSortants; // Liste des arcs sortants
    List<Voiture> cars; // Liste des voitures sur noeud
    double id; // Identifiant du noeud double construit a partir des coordonnes  (x,y)

	// Lien vers l'application graphique
	AppliTrafic appli;


   // Constructeur initialisant les coordonnees du noeud et son type
   Noeud(int _x, int _y) {
      x = _x;
      y = _y;
      double yf = y/((Math.floor(Math.log10(y))+1)*10);
      id = (double)x+yf;
      noeudsAccessibles = new ArrayList<>();
      arcEntrants = new ArrayList<>();
      arcSortants = new ArrayList<>();
   }
	
	// Affiche les coordonnees d'un noeud
	public String toString() {		
		return "noeud (" + x +"," + y + ")" ;
	}
   
   // Deux noeuds sont egaux s'ils ont la meme id
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return id == ((Noeud)o).id;
   }

   public List<Noeud> getVoisins() {
	   return noeudsAccessibles;
   }
   public List<Arc> getArcSortants() {
	   return arcSortants;
   }	
   public double getId() {
	   return id;
   }

}
