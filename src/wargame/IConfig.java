package wargame;

import java.awt.Color;

public interface IConfig {
	int LARGEUR_CARTE = 36;
	int HAUTEUR_CARTE = 20; // en nombre de cases
	int NB_PIX_CASE = 30;
	int POSITION_X = 100;
	int POSITION_Y = 50; // Position de la fenetre
	int NB_HEROS = 3;
	int NB_MONSTRES = 5;
	int NB_OBSTACLES = 10;
	Color COULEUR_VIDE = Color.white, COULEUR_INCONNU = Color.lightGray;
	Color COULEUR_TEXTE = Color.black, COULEUR_MONSTRES = Color.red;
	Color COULEUR_HEROS = Color.blue, COULEUR_HEROS_DEJA_JOUE = new Color(108, 166, 205);
	Color COULEUR_EAU = Color.cyan, COULEUR_FORET = Color.green, COULEUR_ROCHER = Color.gray;

}
