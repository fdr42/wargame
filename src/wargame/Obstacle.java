package wargame;

import java.awt.Color;
import java.awt.Graphics;

public class Obstacle extends Element {
	/**
	 * Classe permetant de gerer les obstacle du jeu (rocher, eau, foret)
	 */
	private static final long serialVersionUID = 1L;
	public Position pos;

	
	public enum TypeObstacle {
		ROCHER(IConfig.COULEUR_ROCHER), FORET(IConfig.COULEUR_FORET), EAU(IConfig.COULEUR_EAU);
		private final Color COULEUR;

		TypeObstacle(Color couleur) {
			COULEUR = couleur;
		}

		public static TypeObstacle getObstacleAlea() {
			return values()[(int) (Math.random() * values().length)];
		}
	}

	private TypeObstacle TYPE;

	/**
	 * Constructeur d'un obstacle
	 * @param type Type de l'obstacle (Rocher, Eau, Foret)
	 * @param pos Position de l'obstacle sur la carte
	 */
	Obstacle(TypeObstacle type, Position pos) {
		TYPE = type;
		this.pos = pos;
		this.vide = false;
		this.estVisible = false;
	}
	

	/**
	 * Renvoie une chaine contenant le type de l'obstacle si il est visible sur la carte
	 */
	public String toString() {
		if (estVisible)
			return "" + TYPE;
		return "";
	}
	
	
	/**
	 * Permet de recuperer la couleur d'un obstacle
	 * @return
	 */
	public Color getColor() {
		return TYPE.COULEUR;
	}
	
	
	
	/**
	 * Permet de dessiner graphiquement un obstacle 
	 */
	public void seDessiner(Graphics g) {
		if (this.estVisible)
			g.setColor(getColor());
		else
			g.setColor(IConfig.COULEUR_INCONNU);
		g.fillRect(20 + pos.getX() * IConfig.NB_PIX_CASE + 1, 20 + pos.getY() * IConfig.NB_PIX_CASE + 1,
				IConfig.NB_PIX_CASE - 1, IConfig.NB_PIX_CASE - 1);
		g.setColor(Color.black);
		// Si il est visible, on dessine l'obstacle
	}

}
