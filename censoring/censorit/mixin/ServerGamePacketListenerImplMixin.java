package net.tvc.backend.censorit.mixin;

import net.tvc.backend.censorit.CensorIt;

import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Inject(method = "handleEditBook", at = @At("HEAD"), cancellable = true)
    private void censorBookEdit(ServerboundEditBookPacket packet, CallbackInfo ci) {
        if (CensorIt.getBookCensor() != null) {
            List<String> pages = packet.pages();
            Optional<String> title = packet.title();
            
            boolean needsCensoring = false;
            
            // Check pages for banned words
            for (String page : pages) {
                if (page != null && CensorIt.getBookCensor().containsBannedWord(page)) {
                    needsCensoring = true;
                    break;
                }
            }
            
            // Check title for banned words if signing
            if (title.isPresent() && CensorIt.getBookCensor().containsBannedWord(title.get())) {
                needsCensoring = true;
            }
            
            if (needsCensoring) {
                // Censor the pages
                for (int i = 0; i < pages.size(); i++) {
                    String page = pages.get(i);
                    if (page != null && CensorIt.getBookCensor().containsBannedWord(page)) {
                        String censoredPage = CensorIt.getBookCensor().censorBookText(page);
                        pages.set(i, censoredPage);
                    }
                }
                
                // Censor the title if present
                if (title.isPresent() && CensorIt.getBookCensor().containsBannedWord(title.get())) {
                    String censoredTitle = CensorIt.getBookCensor().censorBookTitle(title.get());
                    // Note: We can't easily modify the title in the packet, so we'll log it
                    CensorIt.LOGGER.info("Censored book title from '{}' to '{}'", title.get(), censoredTitle);
                }
            }
        }
    }
}