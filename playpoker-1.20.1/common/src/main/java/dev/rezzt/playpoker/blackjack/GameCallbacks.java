package dev.rezzt.playpoker.blackjack;

import net.minecraft.world.item.ItemStack;

/**
 * Callback interface used by {@link BlackjackGame} to emit side effects (chat, sound, particles,
 * payouts and visual updates) without depending on Minecraft server classes directly.
 */
public interface GameCallbacks {

    void dealerChat(String message);

    void chat(String translationKey);

    void chatToGroup(int group, String translationKey);

    void sendInGameError();

    void playSound(int group, String soundId, float volume, float pitch);

    void spawnParticle(int group, String particleId);

    void returnBet(int group, ItemStack bet);

    void loseBet(int group, ItemStack bet);

    void payoutWin(int group, ItemStack bet);

    void payoutBlackjack(int group, ItemStack bet);

    void showPlayerCard(int group, int slot, BlackjackCard card);

    void showDealerCard(int slot, BlackjackCard card, boolean hidden);

    void revealDealerHoleCard();

    void showBust(int group);

    void broadcastDealerBust(int dealerValue);

    void resetVisuals();
}
