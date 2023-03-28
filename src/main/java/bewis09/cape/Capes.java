package bewis09.cape;

public class Capes {

    public static final AbstractCape GOLDEN_CREEPER = new AnimatedCape(32,"golden_creeper_%20",80,false);
    public static final AbstractCape MINECON_2011 = new Cape("minecon2011");
    public static final AbstractCape WORLD = new Cape("world");

    public static final AbstractCape[] CAPES = {
            null,
            GOLDEN_CREEPER,
            MINECON_2011,
            WORLD
    };
}
