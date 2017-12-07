package wargame;

import java.awt.Color;

public interface ISoldat {
	/**
	 * Interface des soltats
	 * 2 types : Heros et Monstres
	 */
	static enum TypesH {
		HUMAIN(40, 3, 10, 2,new Color(0,100,100)), NAIN(80, 1, 20, 0,new Color(0,100,20)), ELF(70, 5, 10, 6,new Color(70,130,70)), HOBBIT(20, 3, 5, 2,new Color(0,70,200));

		private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR;
private final Color COULEUR;
		TypesH(int points, int portee, int puissance, int tir,Color couleur) {
			POINTS_DE_VIE = points;
			PORTEE_VISUELLE = portee;
			PUISSANCE = puissance;
			TIR = tir;
			COULEUR=couleur;
		}

		public int getPoints() {
			return POINTS_DE_VIE;
		}

		public int getPortee() {
			return PORTEE_VISUELLE;
		}

		public int getPuissance() {
			return PUISSANCE;
		}
		public Color getCouleur() {
			return COULEUR;
		}

		public int getTir() {
			return TIR;
		}

		public static TypesH getTypeHAlea() {
			return values()[(int) (Math.random() * values().length)];
		}
	}

	public static enum TypesM {
		TROLL(100, 1, 30, 0,new Color(255,100,70)), ORC(40, 2, 10, 3,new Color(255,150,0)), GOBELIN(20, 2, 5, 2,new Color(255,200,0));

		private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR;
		private final Color COULEUR;
		TypesM(int points, int portee, int puissance, int tir,Color couleur) {
			POINTS_DE_VIE = points;
			PORTEE_VISUELLE = portee;
			PUISSANCE = puissance;
			TIR = tir;
			COULEUR=couleur;
		}

		public int getPoints() {
			return POINTS_DE_VIE;
		}

		public int getPortee() {
			return PORTEE_VISUELLE;
		}

		public int getPuissance() {
			return PUISSANCE;
		}

		public int getTir() {
			return TIR;
		}

		public Color getCouleur() {
			return COULEUR;
		}
		
		public static TypesM getTypeMAlea() {
			return values()[(int) (Math.random() * values().length)];
		}
	}

	public int getTour();

	void joueTour(int tour);

	void combat(Soldat soldat);

	void seDeplace(Position newPos);
}
