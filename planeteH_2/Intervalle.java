package planeteH_2;

public class Intervalle {

  public int min;
  public int max;

  public static final int PLUS_INFINITY = Integer.MAX_VALUE;
  public static final int MINUS_INFINITY = Integer.MIN_VALUE;

  public Intervalle(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public boolean verify() {
    return min < max;
  }


}
