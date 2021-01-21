package modele;

import java.util.List;

public class Arc {
	
   private Noeud start; // Noeud de depart
   private Noeud end; // Noeud d'arrivee
   List<Voiture> cars; // Liste des voitures sur noeud
   String name; // Nom de l'arc

   public Arc() {}

   public Arc(Noeud start, Noeud end) {
      this.start = start;
      this.end = end;
      this.name = ""+start.id+"-"+end.id;
   }

   public Noeud getStart() {
	   return start;
   }
   public Noeud getEnd() {
	   return end;
   }

   public void setStart(Noeud start) {
	   this.start = start;
   }
   public void setEnd(Noeud end) {
	   this.end = end;
   }
}
