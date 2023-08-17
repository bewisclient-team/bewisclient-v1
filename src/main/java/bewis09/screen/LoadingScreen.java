package bewis09.screen;

import bewis09.util.TextHelper;
import net.minecraft.client.gui.DrawContext;

public class LoadingScreen extends AbstractOptionScreen {
    private final Action action;

    static boolean bl = false;

    public LoadingScreen(Action action) {
        this.action = action;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        context.drawCenteredTextWithShadow(textRenderer, TextHelper.getText("loading"),width/2,height/3,-1);
        if(bl)
            action.action();
        bl = true;
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        if(bl)
            action.action();
    }

    @Override
    public void close() {}

    public interface Action {
        void action();
    }

    @Override
    public int maxHeight() {
        return 0;
    }
}
