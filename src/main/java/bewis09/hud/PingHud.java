package bewis09.hud;

import bewis09.screen.WidgetScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.List;

public class PingHud extends LineHudElement {
    public PingHud() {
        super(7, new CpsHud().getY()+new CpsHud().getHeight()+2,Horizontal.RIGHT,Vertical.TOP,70, List.of(Text.of("")),true);
    }

    @Override
    public String getId() {
        return "PING";
    }

    @Override
    public List<Text> getTexts() {
        if(MinecraftClient.getInstance().isInSingleplayer()) {
            if(MinecraftClient.getInstance().currentScreen instanceof WidgetScreen)
                return List.of(Text.of("Ping: -1"));
            return List.of();
        }
        if(getLatency()==0)
            return List.of(Text.of("Loading..."));
        return List.of(Text.of("Ping: "+getLatency()+" ms"));
    }

    private int getLatency() {
        assert MinecraftClient.getInstance().player != null;
        List<PlayerListEntry> playerListEntries = MinecraftClient.getInstance().player.networkHandler.getListedPlayerListEntries().stream().toList();
        for (PlayerListEntry entry : playerListEntries) {
            if (entry.getProfile().getId().equals(MinecraftClient.getInstance().getSession().getUuidOrNull())) {
                return entry.getLatency();
            }
        }
        return -1;
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }
}