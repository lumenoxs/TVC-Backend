package top.blackmare.bloodred.mixin;

import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.blackmare.bloodred.CensorIt;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {

    @Inject(method = "updateSignText", at = @At("HEAD"))
    private void censorSignText(net.minecraft.world.entity.player.Player player, boolean front, java.util.List<Component> messages, CallbackInfo ci) {
        if (CensorIt.getSignCensor() != null && messages != null) {
            for (int i = 0; i < messages.size(); i++) {
                Component message = messages.get(i);
                if (message != null) {
                    String originalText = message.getString();
                    
                    if (CensorIt.getSignCensor().containsBannedWord(originalText)) {
                        String censoredText = CensorIt.getSignCensor().censorSignText(originalText);
                        messages.set(i, Component.literal(censoredText));
                    }
                }
            }
        }
    }
}