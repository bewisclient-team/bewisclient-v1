package bewis09.wings;

import net.minecraft.util.Identifier;

public record Wing(Identifier texture) {
    public static final Wing EMPTY = new Wing(new Identifier("bewisclient", "gui/nothing.png"));
    public static final Wing WING1 = new Wing(new Identifier("bewisclient","wing/wing1.png"));
    public static final Wing FIRE = new Wing(new Identifier("bewisclient","wing/fire.png"));
    public static final Wing[] WINGS = {
            EMPTY,WING1,FIRE
    };
    public static Wing current_wing = EMPTY;

}
