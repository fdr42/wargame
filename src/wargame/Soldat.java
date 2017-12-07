package wargame;

import java.awt.Color;

public abstract class Soldat extends Element implements ISoldat {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	private final Color COULEUR;
	public int tour = 0;
	public final Carte carte;
	public Position pos;
	public Position tabPortee[];

	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos,Color couleur) {

		POINTS_DE_VIE_MAX = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
		this.vide = false;
		tabPortee = pos.remplieTab(getPortee());// On initialise les tableaux
		COULEUR=couleur;
		if (this instanceof Heros)
			vision();// On rends visible les cases a portée

	}

	public int getPoints() {
		return pointsDeVie;
	}

	public int getPointsMax() {
		return POINTS_DE_VIE_MAX;
	}

	public int getPortee() {
		return PORTEE_VISUELLE;
	}

	public int getTir() {
		return TIR;
	}

	public int getTour() {
		return tour;
	}
	public Color getCouleur() {
		return COULEUR;
	}

	public void combat(Soldat soldat) {
		if (soldat.pos.estVoisine(pos))
			soldat.pointsDeVie -= this.getPuissance();
		else
			soldat.pointsDeVie -= this.getTir();
		if (soldat.pointsDeVie <= 0) {
			carte.mort(soldat);

			if (this instanceof Heros)
				vision();// On rends visible les cases a portée
		}

		joueTour(carte.tour);
	}

	public void joueTour(int tour) {// On joue un tour
		carte.num++;// On incremente le compteur num
		this.tour = tour;

		if ((carte.nbHeros + carte.nbMonstre) * carte.tour - carte.nbMonstre <= carte.num && this instanceof Heros) {
			carte.jouerSoldats();
		}
	}

	public int getPuissance() {
		return PUISSANCE;
	}

	public void vision() {
		int i;
		tabPortee = pos.remplieTab(getPortee());// On initialise les tableaux
		if (this instanceof Monstre)
			return;
		for (i = 0; i < tabPortee.length; i++) {// On rends visible les positions a portée
			if (tabPortee[i].estValide())
				carte.getElement(tabPortee[i]).setVisible();

		}
		setVisible();
	}

	public void seDeplace(Position newPos) {
		int i, j;
		Element soldat;
		if (newPos.estValide()) {
			if (this instanceof Heros) {
				for (i = 0; i < tabPortee.length; i++) // On rends invsible les positions a portée avant le deplacement
					if (tabPortee[i].estValide())
						carte.getElement(tabPortee[i]).setInvisible();

				pos = newPos;

				for (i = 0; i < IConfig.LARGEUR_CARTE; i++)
					for (j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
						soldat = carte.getElement(new Position(i, j));
						if (soldat instanceof Heros)
							((Soldat) soldat).vision();
					}

			} else
				pos = newPos;
		}
	}

	public void repos() {
		pointsDeVie++;
		joueTour(carte.tour);
	}

	public String getType() {
		// TODO Auto-generated method stub
		return "";
	}

}
