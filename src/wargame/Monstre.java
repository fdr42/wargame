package wargame;

import java.awt.Color;
import java.awt.Graphics;

public class Monstre extends Soldat {
	/**
	 * Classe pour gerer les monstres
	 */
	private static final long serialVersionUID = 1L;
	public String NOM;
	public TypesM TYPE;
	Heros cible = null;
	boolean traversee = false;

	/**
	 * Constructeur d'un monstre
	 * @param carte Carte associee au monstre
	 * @param type Le type de monstre (gobelin, troll ...)
	 * @param nom Le nom du monstre
	 * @param pos La position du monstre
	 */
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos,type.getCouleur());
		NOM = nom;
		TYPE = type;

	}

	/**
	 * Permet de recuperer le type du monstre
	 */
	public String getType() {
		return "" + TYPE;
	}

	/**
	 * Permet de cibler un heros (utilisee dans l'IA
	 * @param hero Hero a cibler
	 */
	public void setCible(Heros hero) {
		cible = hero;
	}

	
	/**
	 * Permet de dessiner graphiquement le monstre
	 */
	public void seDessiner(Graphics g) {
		Color couleur;

		if (getTour() == carte.tour)
			couleur = IConfig.COULEUR_HEROS_DEJA_JOUE;
		else if (!this.estVisible)
			couleur = IConfig.COULEUR_INCONNU;
		else
			couleur = this.getCouleur();
		g.setColor(couleur);
		g.fillRect(20 + pos.getX() * IConfig.NB_PIX_CASE + 1, 20 + pos.getY() * IConfig.NB_PIX_CASE + 1,
				IConfig.NB_PIX_CASE - 1, IConfig.NB_PIX_CASE - 1);
		if (!this.estVisible)
			couleur = IConfig.COULEUR_INCONNU;
		else
			g.setColor(new Color(255,0,0,150));
		g.fillOval(23 + pos.getX() * IConfig.NB_PIX_CASE + 1, 23 + pos.getY() * IConfig.NB_PIX_CASE + 1,
				IConfig.NB_PIX_CASE - 7, IConfig.NB_PIX_CASE - 7);
		
		g.setColor(Color.white);
		if (this.estVisible)
			g.drawString("" + NOM, 24 + pos.getX() * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE / 4,
					pos.getY() * IConfig.NB_PIX_CASE + 20 + (IConfig.NB_PIX_CASE - IConfig.NB_PIX_CASE / 3));
		g.setColor(Color.black);
		// On affiche le monstre avec une couleur dependant du fait qu'il ait joué ou
		// non
		// Avec son nom
	}

	
	/**
	 * Renvoie les differentes caracteristique d'un monstre
	 */
	public String toString() {
		if (estVisible)
			return TYPE + " " + NOM + " (" + this.getPoints() + "/" + this.getPointsMax() + "PV)";
		return "";
	}
}
