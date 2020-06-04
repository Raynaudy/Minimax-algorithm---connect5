package Connect5Game;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import planeteH_2.*;


public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();
    public long startChrono;
    public int delais;


    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans JoueurArtificiel.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package planeteH_2.ia.
     * Vous ne pouvez pas modifier les fichiers directement dans planeteH_2., car ils seront écrasés.
     *
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */

    @Override
    public Position getProchainCoup(Grille grille, int delais) {
      this.startChrono = System.currentTimeMillis();
      this.delais = delais;
      Etat e = new Etat(grille, new Position(0, 0), false, new Intervalle(Intervalle.MINUS_INFINITY, Intervalle.PLUS_INFINITY), this);
      Position decision = null;
      int i = 1;
      while(true) {
        Position decisionTemp = minMaxDecision(e, i);
        if (decisionTemp != null)
          decision = decisionTemp;
        if (System.currentTimeMillis() - this.startChrono > delais) break;

        i++;
        //System.out.println("depth: " + i);
      }
      return decision;
        /*ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int nbcol = grille.getData()[0].length;
        for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    casesvides.add(l*nbcol+c);
        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);*/
    }

    public Position minMaxDecision(Etat e, int depth) {
      List<Etat> s = e.successeurs(depth);
      //System.out.println("nb successeurs: " + s.size());
      Integer maxvalue = null;

      if(e.fini)
        return e.position;

    /*  for(Etat es: s){
        es.value = minimaxValue(es);
      }

      if(e.joueur == e.joueurCourant)
        maxvalue = Etat.maxValue(s);
      else maxvalue = Etat.minValue(s);
      System.out.println("Score: " + maxvalue);*/

      Position result=e.position;
      if (s == null) return null;
      for(Etat em : s){
        if(maxvalue == null || em.value > maxvalue){
          result =  em.position;
          maxvalue = em.value;
        }

      }
      //System.out.println(maxvalue);

      //par default au cas où
      return result;
    }

}
