package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public class SleepTimerHud extends LineHudElement {
    public SleepTimerHud() {
        super(7, new DayHud().getY()+new DayHud().getHeight()+2, Horizontal.RIGHT, Vertical.TOP, 70, List.of(Text.of("")), false);
    }

    @Override
    public String getId() {
        return "SLEEP";
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }

    @Override
    public List<Text> getTexts() {
        assert MinecraftClient.getInstance().world != null;
        return List.of(Text.of((
                withZero((int)((MinecraftClient.getInstance().world.getTimeOfDay()+6000)%24000) /1000
        ))+":"+ withZero((int)(((int)((MinecraftClient.getInstance().world.getTimeOfDay()+6000)%24000)%1000)/1000f*60))));
    }

    @Override
    public void paint(DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().scale(getSize(),getSize(),getSize());
        context.fill(getX(), getY(), getX()+getWidth(), getY()+getHeight(),getColor());
        assert MinecraftClient.getInstance().world != null;
        fillLine(context,18541, 24000, 0xFF333399, true);
        fillLine(context,0, 5459, 0xFF333399, true);
        fillLine(context,5459, 18541, 0xFF993333, true);
        fillLine(context,18541, 24000, 0xFF333399, false);
        fillLine(context,0, 5459, 0xFF333399, false);
        fillLine(context,5459, 18541, 0xFF993333, false);
        int i = -1;
        for (Text text : getTexts()) {
            i++;
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, text, getX()+getWidth()/2, getY()+i*11+3,-1);
        }
        context.getMatrices().pop();
    }

    public void fillLine(DrawContext context, int time1, int time2, int color, boolean falseTime) {
        assert MinecraftClient.getInstance().world != null;
        int currentTime = falseTime ? 23999 : (int) ((MinecraftClient.getInstance().world.getTimeOfDay() + 6000) % 24000);
        if(time1<currentTime)
            context.fill((int) (getX()+3+ (64/24000f)*time1),getY()+3, (int) (getX()+3+ (64/24000f)*Math.min(time2,currentTime)), getY()+11, color-(falseTime?0xCC000000:0));
    }

    public String withZero(int i) {
        if(String.valueOf(i).length()==1) {
            return "0" + i;
        }
        return String.valueOf(i);
    }
}
