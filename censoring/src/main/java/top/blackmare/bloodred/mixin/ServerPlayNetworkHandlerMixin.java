package top.blackmare.bloodred.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.blackmare.bloodred.CensorIt;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {
    
    @Shadow
    public ServerPlayer player;
    
    @Inject(method = "broadcastChatMessage", at = @At("HEAD"), cancellable = true)
    private void censorChatMessage(PlayerChatMessage message, CallbackInfo ci) {
        if (CensorIt.getChatCensor() != null && message != null) {
            String originalContent = message.signedContent();
            
            if (CensorIt.getChatCensor().containsBannedWord(originalContent)) {
                String censoredContent = CensorIt.getChatCensor().censorMessage(originalContent);
                
                // Cancel the original message
                ci.cancel();
                
                // Send a system message with the censored content to all players
                if (this.player != null && this.player.getServer() != null) {
                    Component censoredText = Component.literal("<" + this.player.getName().getString() + "> " + censoredContent);
                    this.player.getServer().getPlayerList().broadcastSystemMessage(censoredText, false);
                }
            }
        }
    }
}