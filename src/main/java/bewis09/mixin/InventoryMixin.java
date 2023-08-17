package bewis09.mixin;

import bewis09.main.Main;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class InventoryMixin extends Screen {

    @Shadow @Final protected ScreenHandler handler;

    @Shadow protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    protected InventoryMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed",at=@At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if(Main.ElytraSwitcher.matchesKey(keyCode, scanCode)) {
            try {
                Slot s = null;
                Slot g = null;
                for (Slot l : this.handler.slots) {
                    if (l.getStack().getItem() == Items.ELYTRA) {
                        s = l;
                    }
                }
                for (Slot l : this.handler.slots) {
                    if (l.getStack().getItem() == Items.NETHERITE_CHESTPLATE) {
                        g = l;
                    }
                }
                assert g != null;
                assert s != null;
                if (s.id > g.id) {
                    this.onMouseClick(s, s.id, 8, SlotActionType.SWAP);
                    this.onMouseClick(g, g.id, 8, SlotActionType.SWAP);
                    this.onMouseClick(s, s.id, 8, SlotActionType.SWAP);
                }
                if (s.id < g.id) {
                    this.onMouseClick(g, g.id, 8, SlotActionType.SWAP);
                    this.onMouseClick(s, s.id, 8, SlotActionType.SWAP);
                    this.onMouseClick(g, g.id, 8, SlotActionType.SWAP);
                }
                cir.setReturnValue(true);
            } catch (Exception ignored) {
            }
        }
    }
}
