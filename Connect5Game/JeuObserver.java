/*
 * INF4230 - Intelligence artificielle
 * TP2 - Algorithme minimax avec élagage alpha-beta appliqué au jeu Connect5GUI
 *
 * (C) Éric Beaudry 2016.
 * UQAM - Département d'informatique
 */

package Connect5Game;

/**
 *
 * @author Eric Beaudry
 */
public interface JeuObserver {

    public void grilleChanged(Grille g);
    
    public void message(String msg);
    
}
