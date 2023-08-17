package bewis09.cape;

import bewis09.util.Log;
import bewis09.util.MathUtil;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AnimatedCape extends AbstractCape {

    private final int frameCount;
    private final List<Identifier> identifierList;
    private final int frameDuration;

    public AnimatedCape(int frameCount, String name, int frameDuration, boolean withZero) {
        this.frameCount = frameCount;
        this.frameDuration = frameDuration;
        identifierList = new ArrayList<>();
        for (int i = 0; i < frameCount; i++) {
            identifierList.add(new Identifier("bewisclient","cape/"+name.replace("%20", MathUtil.zeroBefore(i,withZero?2:1))+".png"));
        }
        startTimer();
    }

    @Override
    int getFrameDuration() {
        Log.log(frameDuration);
        return frameDuration;
    }

    @Override
    int getFrameCount() {
        return frameCount;
    }

    @Override
    public Identifier getIdentifier(int frame) {
        return identifierList.get(frame);
    }
}
