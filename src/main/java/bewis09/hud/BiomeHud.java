package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BiomeHud extends LineHudElement {
    public BiomeHud() {
        super(7, 7, Horizontal.LEFT, Vertical.BOTTOM, 100, List.of(Text.of("")), true);
    }

    @Override
    public String getId() {
        return "BIOME";
    }

    @Override
    public List<Text> getTexts() {
        AtomicReference<Text> text = new AtomicReference<>(Text.of(""));
        assert MinecraftClient.getInstance().world != null;
        assert MinecraftClient.getInstance().cameraEntity != null;
        MinecraftClient.getInstance().world.getBiome(MinecraftClient.getInstance().cameraEntity.getBlockPos()).getKey().ifPresent(biomeRegistryKey -> {
            text.set(Text.translatable(biomeRegistryKey.getValue().toTranslationKey("biome")));
        });
        return List.of(text.get());
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }
}
