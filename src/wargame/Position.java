package wargame;

import java.io.Serializable;

public class Position implements IConfig, Serializable {
	/**
	 * Classe permetant de gerer les positions dans la carte.
	 */
	private static final long serialVersionUID = -3773706663664931953L;
	/**
	 * 
	 */
	private int x, y;

	/**
	 * Constructeur d'une position en cases
	 * @param x Position x en cases
	 * @param y Position y en cases
	 */
	Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Permet de recuperer la variable x d'une position 
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	
	/**
	 * Permet de recuperer la variable y d'une position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Permet de modifier la variable x d'une postion
	 * @param x Valeur qui va remplacer la valeur d'origine
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Permet de modifier la variable x d'une postion
	 * @param y Valeur qui va remplacer la valeur d'origine
	 */
	public void setY(int y) {
		this.y = y;
	}

	
	/**
	 * Permet de verifier si une position est valide (a l'interieur de la carte)
	 * @return
	 */
	public boolean estValide() {
		if (x < 0 || x >= LARGEUR_CARTE || y < 0 || y >= HAUTEUR_CARTE)
			return false;
		else
			return true;
	}

	
	/**
	 * Renvoie une chaine contenant la position (x,y)
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	
	/**
	 * Renvoie vrais si la position est voisine (adjacente) a la position passee en parametre. Faux sinon
	 * @param pos
	 * @return
	 */
	public boolean estVoisine(Position pos) {
		return (((Math.abs(x - pos.x) <= 1) && (Math.abs(y - pos.y) <= 1)));
	}

	
	/**
	 * Renvoie vrai si une position est egale a la position passee en parametre 
	 * @param pos
	 * @return
	 */
	public boolean equals(Position pos) {
		if (x == pos.getX() && y == pos.getY())
			return true;
		return false;
	}

	
	/**
	 * Calcule la distance entre deux positions
	 * @param pos
	 * @return
	 */
	public double distance(Position pos) {
		double difX = Math.pow(pos.getX() - this.x, 2);
		double difY = Math.pow(pos.getY() - this.y, 2);
		return Math.sqrt(difX + difY);
	}

	
	
	public Position[] remplieTab(int po) {// Fonction qui remplie les tableaux
		Position[] tab;

		tab = new Position[(po * 2 + 1) * (po * 2 + 1)];// Pour le tableau portee

		int i, j, n = 0;
		for (i = -po; i <= po; i++)
			for (j = -po; j <= po; j++) {
				tab[n] = new Position(x + i, y + j);
				n++;

			}
		return tab;
	}
}
