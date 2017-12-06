package wargame;

import java.awt.Color;
import java.awt.Graphics;

public class Heros extends Soldat {
	/**
	 * 
	 */
	private static final long serialVersionUID = -232245575244741042L;
	/**
	 * Classe de gestion des heros (personnages controle par le joueur)
	 */
	public String NOM;
	public TypesH TYPE;

	/**
	 * Constructeur du hero
	 * @param carte Carte associee au hero
	 * @param type Type du hero (humain, nain ...)
	 * @param nom Nom du hero
	 * @param pos Position du hero
	 */
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}

	/**
	 * Permet de dessiner graphiquement le hero
	 */
	public void seDessiner(Graphics g) {
		Color couleur;
		if (getTour() == carte.tour)
			couleur = IConfig.COULEUR_HEROS_DEJA_JOUE;
		else
			couleur = IConfig.COULEUR_HEROS;

		g.setColor(couleur);
		g.fillRect(20 + pos.getX() * IConfig.NB_PIX_CASE + 1, pos.getY() * IConfig.NB_PIX_CASE + 21,
				IConfig.NB_PIX_CASE - 1, IConfig.NB_PIX_CASE - 1);
		g.setColor(Color.white);
		g.drawString("" + NOM, 20 + pos.getX() * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE / 4,
				pos.getY() * IConfig.NB_PIX_CASE + 20 + (IConfig.NB_PIX_CASE - IConfig.NB_PIX_CASE / 3));
		g.setColor(Color.black);
		// On affiche le hero avec une couleur dependant du fait qu'il ait joué ou non
		// Avec son nom
	}

	
	/**
	 * Teste si le hero est egal au hero en parametre
	 * @param bis Le hero a comparer avec notre hero
	 * @return retour true si les deux heros sont egaux, false sinon
	 */
	public boolean equals(Heros bis) {
		if (this.NOM.equals(bis.NOM))
			return true;
		return false;

	}
	
	
	/**
	 * Renvoie le type du hero
	 */
	public String getType() {
		return "" + TYPE;
	}

	
	/**
	 * Donne les informations sur le hero (type, nom, point de vie, point de vie max)
	 */
	public String toString() {

		return TYPE + " " + NOM + " (" + this.getPoints() + "/" + this.getPointsMax() + "PV)";
	}
}
