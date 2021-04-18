package beehivehoney;
import honey.Honey;
public class BeeHiveHoney implements Honey {
  public double eatSome() {
    return Math.random();
  }
  public void nonInterfaceMethod() {
    System.out.println("BeeHiveHoney.nonInterfaceMethod() invoked - wasn't in the Honey interface");
  }
}