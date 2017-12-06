package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;


public class Element implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6775807363055912152L;
	/**
	 * Classe de gestion des differents elements du jeu
	 */
	Position pos = new Position(0, 0);
	public boolean estVisible = false;
	public boolean vide = true;

	Element() {
	}

	
	/**
	 * Methode qui rend l'element visible
	 */
	public void setVisible() {
		estVisible = true;
	}

	
	/**
	 * Methode qui rend l'element invisible
	 */
	public void setInvisible() {
		estVisible = false;
	}

	
	/**
	 * Verifie si l'element est vide
	 */
	public boolean estVide() {
		return vide;
	}

	
	
	public String toString() {
		if (estVisible)
			return "Position vide";

		return "";
	}

	
	
	/**
	 * Stocke la position pos
	 */
	public void recupPos(Position pos) {
		this.pos = pos;
	}

	
	
	/**
	 * Dessine graphiquement l'element (couleur,nom,position)
	 */
	public void seDessiner(Graphics g) {
		if (!estVisible) // Si une case est visible, on la met blanche sinon grise
			g.setColor(IConfig.COULEUR_INCONNU);
		else
			g.setColor(Color.white);

		g.fillRect(20 + pos.getX() * IConfig.NB_PIX_CASE + 1, 20 + pos.getY() * IConfig.NB_PIX_CASE + 1,
				IConfig.NB_PIX_CASE - 1, IConfig.NB_PIX_CASE - 1);
		g.setColor(Color.black);
	}

}
