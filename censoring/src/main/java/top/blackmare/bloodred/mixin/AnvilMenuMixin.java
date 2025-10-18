package top.blackmare.bloodred.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.blackmare.bloodred.CensorIt;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Shadow
    public String itemName;

    @Inject(method = "createResult", at = @At("HEAD"))
    private void censorAnvilItemName(CallbackInfo ci) {
        if (CensorIt.getAnvilCensor() != null && this.itemName != null && !this.itemName.isEmpty()) {
            if (CensorIt.getAnvilCensor().containsBannedWord(this.itemName)) {
                String censoredName = CensorIt.getAnvilCensor().censorItemName(this.itemName);
                this.itemName = censoredName;
            }
        }
    }
}