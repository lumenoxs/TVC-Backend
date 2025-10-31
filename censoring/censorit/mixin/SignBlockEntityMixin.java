package net.tvc.backend.censorit.mixin;

import net.tvc.backend.censorit.CensorIt;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {
    @Inject(method = "updateSignText", at = @At("HEAD"))
    private void censorSignText(PlayerEntity player, boolean front, List<Text> messages, CallbackInfo ci) {
        if (CensorIt.getSignCensor() != null && messages != null) {
            for (int i = 0; i < messages.size(); i++) {
                Text message = messages.get(i);
                if (message != null) {
                    String originalText = message.getString();
                    
                    if (CensorIt.getSignCensor().containsBannedWord(originalText)) {
                        String censoredText = CensorIt.getSignCensor().censorSignText(originalText);
                        messages.set(i, Text.literal(censoredText));
                    }
                }
            }
        }
    }
}