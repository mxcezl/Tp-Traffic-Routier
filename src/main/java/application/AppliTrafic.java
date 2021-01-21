package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.Arc;
import modele.Noeud;
import modele.ReseauRoutier;
import modele.Voiture;
import modele.VoitureTempRemove;

public class AppliTrafic extends Application implements EventHandler<MouseEvent> {

	int tailleCase;         // Taille d'une case en pixels
	List<Voiture> voitures; // Les voitures
	List<VoitureTempRemove> vehiculesAretirer; // Les voitures
	List<DessinVoiture> dessinsVoitures; // Les dessins des voitures
	Scene scene;            // Scene de jeu
	Group troupe;           // Troupe des acteurs
	long tempo = 1000;      // Vitesse de l'animation
	List<List<Integer>> noeudInitiaux = new ArrayList<>(Arrays.asList(Arrays.asList(0,1), Arrays.asList(0,2), Arrays.asList(0,3), Arrays.asList(0,4),
																	  Arrays.asList(1,5), Arrays.asList(2,5), Arrays.asList(3,5), Arrays.asList(4,5)));

	// Lancement automatique de l'application graphique
	public void start(Stage primaryStage) {
		tailleCase = 100;
		voitures = new ArrayList<>();
		vehiculesAretirer = new ArrayList<>();
		dessinsVoitures = new ArrayList<>();
		construirePlateauJeu(primaryStage);
	}

	// Construction du theatre et de la scene
	void construirePlateauJeu(Stage primaryStage) {

		troupe = new Group(); // Definir la scene principale
		scene = new Scene(troupe, tailleCase + 5 * tailleCase, 2 * tailleCase + 4 * tailleCase, Color.ANTIQUEWHITE);

		// Definir les acteurs et les habiller
		dessinEnvironnement(troupe);

		primaryStage.setTitle("Trafic Routier...");
		primaryStage.setScene(scene);
		
		// Afficher le theatre
		primaryStage.show();

		// Compteur pour le stream :
		// i[0] -> nombre de cycles
		// i[1] -> nombre de voiture pour les id
		int[] i = { 0 , 0 };
		Random rand = new Random();
		// Lancer le timer pour faire vivre la matrice
		Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo), event-> {
			
					vehiculesAretirer.stream().forEach(container -> {
						container.incrementerTemps();
						if(container.getVoiture().isAccidente()) {
							container.getVoiture().getDv().cj = Color.BLACK;
							container.getVoiture().getDv().colorerVoiture();
						}
						if(container.getTemps() >= 3) { // Si ca fait plus de 3 cycles qu'il est en attente de suppression
							
							// Log
							System.out.println("[-] Suppression | Voiture id:" + container.getVoiture().getId() + " supprimé de la carte.");
							
							// Retirer de la liste des voitures
							voitures.remove(container.getVoiture());
							
							// Retirer le dessin de la voiture pour éviter qu'il continue d'être affiché
							dessinsVoitures.remove(container.getVoiture().getDv());
							
							// On retire la voiture du GUI
							troupe.getChildren().remove(container.getVoiture().getDv());
						}
					});
					
					// On le retire au bout de 3 cycles (10 cycles trop lent)
					vehiculesAretirer.removeIf(c -> c.getTemps() >= 3);
					
					// On ajoute des voitures tout les 3 cycles
					if(i[0] % 3 == 0) {
						// Prends au hasard un noeud de depart
						List<Integer> org = noeudInitiaux.get(rand.nextInt(noeudInitiaux.size()));
						// Calcule la destination par rapport au point de départ
						List<Integer> dest = calculerDest(org);

						// Ajoute une nouvelle voiture
						voitures.add(new Voiture(i[1], org.get(0), org.get(1), dest.get(0), dest.get(1)));
						i[1] += 1; // Cache des Id des voitures
						
						// Deuxième voiture ajoutée par le même procédé
						org = noeudInitiaux.get(rand.nextInt(noeudInitiaux.size()));
						dest = calculerDest(org);
						voitures.add(new Voiture(i[1], org.get(0), org.get(1), dest.get(0), dest.get(1)));
						
						i[1] += 1; // Cache des Id des voitures
					}
					
					// Actualisation de l'affichage
					dessinerVoitures();
					animDeplacement();
					
					// On augmente le compteur de cycles
					i[0] += 1;
				}
			));
		
		littleCycle.setCycleCount(Timeline.INDEFINITE);
		littleCycle.play();
	}


	public List<Integer> calculerDest(List<Integer> origine) {
		List<Integer> dest = new ArrayList<>(Arrays.asList(origine.get(0), origine.get(1)));

		// Verifie l'origine du vehicule
		if(dest.get(0) == 0) dest.set(0, 5);
		else if(dest.get(1) == 5) dest.set(1, 0);
		return dest;
	}
	
	// Creation des dessins des routes, carrefours et voitures
	void dessinEnvironnement(Group troupe) {
		ReseauRoutier.creerReseau();
		List<Noeud> noeuds = ReseauRoutier.getNoeuds();

		int decalage = tailleCase / 2;

		// Creation des carrefours
		int radius = decalage / 3;
		for (Noeud noeud : noeuds) {

			int cx = decalage + noeud.getX() * tailleCase;
			int cy = decalage + noeud.getY() * tailleCase;
			Circle c = new Circle(cx, cy, radius);
			c.setFill(Color.BLACK);
			c.setOpacity(0.2);
			troupe.getChildren().add(c);
			c.setSmooth(true);
		}

		// Creation des routes
		for (Noeud noeud:noeuds) {
			int ox = decalage + noeud.getX() * tailleCase;
			int oy = decalage + noeud.getY() * tailleCase;

			for (Arc arc : noeud.getArcSortants()) {
				Noeud dest=arc.getEnd();
				int dx = decalage + dest.getX() * tailleCase;
				int dy = decalage + dest.getY() * tailleCase;
				Line l = new Line(ox, oy, dx, dy);
				l.setStrokeWidth(6);
				l.setStroke(Color.DARKGOLDENROD);
				troupe.getChildren().add(l);
			}
		}
	}

	
	public void dessinerVoitures() {
		int decalage = tailleCase / 2;
		int radius = decalage / 3;
		
		// Dessin des voitures
		for(Iterator<Voiture> iterator = voitures.iterator(); iterator.hasNext(); ) {
			Voiture v = iterator.next();
			if(!v.isArrivee() && v.getDv() == null) {
				System.out.println("[+] Ajout | Voiture id:" + v.getId());
				int cx = decalage + v.getX() * tailleCase;
				int cy = decalage + v.getY() * tailleCase;
				DessinVoiture dv = new DessinVoiture(cx, cy, radius);
				troupe.getChildren().add(dv);
				dv.setSmooth(true);
				dv.setOnMouseClicked(this);
				DropShadow dropShadow = new DropShadow();
				dropShadow.setRadius(5.0);
				dropShadow.setOffsetX(3.0);
				dropShadow.setOffsetY(3.0);
				dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
				dv.setEffect(dropShadow);
				v.setDv(dv);
				dessinsVoitures.add(dv);
			} else if(v.isArrivee()){
				v.getDv().cj = Color.LIGHTGREEN;
				v.getDv().colorerVoiture();
				iterator.remove();
				dessinsVoitures.remove(v.getDv());
				vehiculesAretirer.add(new VoitureTempRemove(v));
			}
		}
	}
	// Reponse aux evenements de souris
	public void handle(MouseEvent me) {

		Object o = me.getSource();
		if (o instanceof DessinVoiture) {
			DessinVoiture dv = (DessinVoiture) o;
			dv.switchSelected();
			Voiture v = voitures.get(dessinsVoitures.indexOf(dv));
			v.setPause(!v.isPause());
		}
	}


	// Realise l'animation
	private void animDeplacement() {

		int decalage = tailleCase / 2;
		for(int i=0; i<voitures.size(); i++) {
			Voiture v = voitures.get(i);
			if(!v.isArrivee() && !v.isPause()) {
				
				// Verifie si il y a un vehicule au meme moment sur le meme noeud
				Voiture vAccident = voitures.stream()
						.filter( vTemp -> (
								vTemp.getId() != v.getId() 
								&& vTemp.getX() == v.getRouteRestante().get(0).getX() 
								&& vTemp.getY() == v.getRouteRestante().get(0).getY())
							)
						.findFirst()
						.orElse(null);
				
				Noeud n = v.prochainNoeud();
				
				// Si il y a encore de la route a faire pour le vehicule courant
				if(null != n) {
					v.setXY(n.getX(), n.getY());
					DessinVoiture dv = dessinsVoitures.get(i);
					if(!dv.selected) {
						Timeline timeline = new Timeline();
						int xdest = decalage + n.getX() * tailleCase;
						int ydest = decalage + n.getY() * tailleCase;
						KeyFrame bougeVoiture = new KeyFrame(new Duration(tempo), 
								new KeyValue(dv.centerXProperty(), xdest),
								new KeyValue(dv.centerYProperty(), ydest));
						timeline.getKeyFrames().add(bougeVoiture);
						timeline.play();
						dv.setAnimation(timeline);
					}
				}
				
				// Si il y a un accident / vehicule accidenté sur la route
				if (vAccident != null) {
					System.out.println("[!] Accident | (" + vAccident.getX() + "," + vAccident.getY() + ")");
					
					vAccident.setAccidente(true);
					vAccident.setPause(true);
					
					v.setAccidente(true);
					v.setPause(true);
					
					vehiculesAretirer.add(new VoitureTempRemove(vAccident));
					vehiculesAretirer.add(new VoitureTempRemove(v));
				}
			} 
		}

	}



	public static void main(String[] args) {
		launch(args);
	}

}
