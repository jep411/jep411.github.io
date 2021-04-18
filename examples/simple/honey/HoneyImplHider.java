package honey;

public class HoneyImplHider implements Honey {
    private final Honey hiddenImpl;
    public HoneyImplHider(Honey honey) {
        this.hiddenImpl = honey;
    }

    public double eatSome() {
        return hiddenImpl.eatSome();
    }
}
