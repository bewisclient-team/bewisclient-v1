package bewis09.cape;

import net.minecraft.util.Identifier;

public class Cape extends AbstractCape {

    final Identifier identifier;

    private static AbstractCape currentCape;

    public Cape(String name) {
        this.identifier = new Identifier("bewisclient","cape/"+name+".png");
    }

    public static AbstractCape getCurrentCape() {
        return currentCape;
    }

    public static void setCurrentCape(AbstractCape currentCape) {
        Cape.currentCape = currentCape;
    }

    @Override
    int getFrameDuration() {
        return 0;
    }

    @Override
    int getFrameCount() {
        return 1;
    }

    @Override
    public Identifier getIdentifier(int frame) {
        return identifier;
    }
}
