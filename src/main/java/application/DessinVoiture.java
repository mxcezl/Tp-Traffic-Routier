package application;


import java.awt.Point;

import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DessinVoiture extends Circle {
	
	boolean selected; // Vrai si le jeton est actuellement selectionne
	private Point position; // Position du jeton dans la grille (null si non pose)
	private Point anciennePosition; // Ancienne position du jeton dans la grille (null si non pose)
	public static Color couleur = Color.RED; // Couleur pour voiture
    public Color cj = couleur; // Couleur courante du jeton
	public static Color couleurSelected = Color.GAINSBORO; // Couleur pour voiture selectionnee
	Timeline animation; // Animation du deplacement


	/**
	*constructeur
	*@param centerX coordonnee X du centre du disque
	*@param centerY coordonnee Y du centre du disque
	*@param radius taille en pixel du rayon du disque
	*@param _joueur numero du joueur associe
	*/
	public DessinVoiture(double centerX, double centerY, double radius) {
		super(centerX, centerY, radius);
		selected = false;
		getCouleur();
		setFill(cj);
	}

	// Active ou desactive la selection du jeton
	public void switchSelected() {
		selected = !selected;
      getCouleur();
      colorerVoiture();
      if(selected && animation!=null) animation.pause();
      if(!selected && animation!=null) animation.play();
	}

	// Definit la bonne couleur pour le jeton en fonction du joueur et de son etat selectionnee ou non
	public Color getCouleur() {
	   cj=(selected?couleurSelected:couleur);	      
		return cj;
	}

	// Remplit le disque avec la couleur courante
	public void colorerVoiture() {
		setFill(cj);
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point _position) {
		this.position = _position;
	}

	public Point getAnciennePosition() {
		return anciennePosition;
	}
	
	public void moveToPosition(Point _point) {
		this.anciennePosition.setLocation(this.position);
		this.position.setLocation(_point);
	}

	public void setAnciennePosition(Point anciennePosition) {
		this.anciennePosition = anciennePosition;
	}
	
	public String toString() {
		return "("+position.x+","+position.y+") avant en "+ "("+anciennePosition.x+","+anciennePosition.y+") ";
	}

   public Timeline getAnimation() {
      return animation;
   }

   public void setAnimation(Timeline animation) {
      this.animation = animation;
   }
}
