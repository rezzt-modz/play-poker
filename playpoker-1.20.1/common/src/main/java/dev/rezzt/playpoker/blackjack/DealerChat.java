package dev.rezzt.playpoker.blackjack;

import java.util.List;
import java.util.Random;

/**
 * Dealer chat lines taken from the 777 datapack.
 *
 * <p>The datapack uses large random pools (100-250 lines per category). To keep the source code
 * maintainable while preserving the feel, a representative subset of each pool is stored here.
 * The random selection behaves identically to the datapack.</p>
 */
public final class DealerChat {

    private static final List<String> START_DEAL = List.of(
            "Everyone is in, let us begin!",
            "Bets are locked. Try not to regret it too quickly.",
            "Alright, hands off the bets. Let us see who did their thinking early.",
            "Bets closed. Time for the cards to start telling the truth.",
            "No more bets. If you wanted to change your mind, you are late.",
            "Bets are settled, smoke is steady, here come the cards.",
            "Bets in, nerves loaded. Let us deal this out.",
            "Everything is placed. Now we find out who was guessing.",
            "Table is closed, deck is open. Dealing.",
            "No more second thoughts. Just first cards."
    );

    private static final List<String> HIT = List.of(
            "Excellent choice.",
            "You are not done yet. Let us scratch that itch.",
            "Another card. Could be genius!",
            "One more coming your way. Do not flinch.",
            "You want more trouble. I can arrange that.",
            "Hit. Always chasing that perfect number, huh?",
            "Another card on the fire. Let us see if it burns.",
            "You tap the felt, the deck answers. Here you go.",
            "Hit it is. No one ever accused you of being cautious.",
            "More ink on your total. Let us add it up."
    );

    private static final List<String> STAND = List.of(
            "You stand there? Alright, let us see if it holds.",
            "Standing...",
            "You are good with that? Bold choice.",
            "Stand, huh?",
            "You lock it in. Let us see if that was wisdom or panic.",
            "Standing there like a statue. Hope it is made of something solid.",
            "You stand. So that is your final answer.",
            "No more cards. You trust that total that much?",
            "You are done. Alright, my turn to make mistakes.",
            "Stand. Let us see if the house respects your confidence."
    );

    private static final List<String> BLACKJACK = List.of(
            "Blackjack. Quick work.",
            "Twenty one on the nose. Not bad.",
            "Try not to look too surprised.",
            "Blackjack pays out. Enjoy it.",
            "Clean twenty one. House tips its hat.",
            "Blackjack. You planning to make this a habit?",
            "Well, look at that. Right to twenty one.",
            "Sharp hand. Blackjack it is.",
            "You hit the ceiling. Blackjack.",
            "Twenty one, just like I promised."
    );

    private static final List<String> BUST_PLAYER = List.of(
            "Too far. You are busted.",
            "Over twenty one. House thanks you.",
            "That is a bust. Ambitious, I will give you that.",
            "Twenty one was back there somewhere. You walked past it.",
            "You crossed the line. Busted.",
            "Too heavy on the draw. That is a bust.",
            "Over the mark. Maybe the next hand will be gentler.",
            "Busted. You were aiming for twenty one, right?",
            "House takes it. You pushed your luck one card too far.",
            "That one snapped. Bust."
    );

    private static final List<String> BUST_DEALER = List.of(
            "Dealer went over. House pays the table.",
            "I pushed it too far. Busted.",
            "Too much confidence on my side. Bust.",
            "The house went past twenty one. Rare, but it happens.",
            "Dealer bust. Your patience paid off."
    );

    private static final List<String> WIN = List.of(
            "You edge out the house. Enjoy that one.",
            "Player beats dealer. Clean, simple, annoying for me.",
            "You come out on top. Do not let it go to your head.",
            "Your total wins. My side blinks first.",
            "You beat the house. That smirk is almost justified.",
            "You are ahead, dealer behind. I will pretend I am happy for you.",
            "You walk away with this one. House will recover... eventually.",
            "Your hand stands taller. Dealer takes the hint.",
            "You win the comparison. I lose a little pride.",
            "Player wins. Go on, enjoy the sound of winnings coming your way."
    );

    private static final List<String> LOSE = List.of(
            "House takes it. You were hoping for a different ending, huh?",
            "That one goes to the house. Happens more than people admit.",
            "You lose this round. Table is not in a generous mood.",
            "Dealer wins. You will blame the cards, I will blame the odds.",
            "Not enough on your side. House pulls it in.",
            "Close, but the house edges you out.",
            "Another one for the house. You keeping score?",
            "You fall short. The house does not.",
            "Dealer takes it. Your hand needed a little more backup.",
            "Not your hand. House walks away with this one."
    );

    private static final List<String> TIE = List.of(
            "Push. Nobody wins, nobody cries.",
            "Tie game. You get your bets, I keep my smug grin.",
            "Same total here.",
            "We meet in the middle. Push.",
            "Dead. Even.",
            "Push. All that drama just to end up even.",
            "Same number, nothing moves.",
            "Tie. How diplomatic.",
            "Push. You get a refund on that little burst of hope.",
            "Even totals. The bets stay put, the tension does not."
    );

    private DealerChat() {
    }

    private static String pick(Random random, List<String> pool) {
        return pool.get(random.nextInt(pool.size()));
    }

    public static String startDeal(Random random) {
        return pick(random, START_DEAL);
    }

    public static String hit(Random random) {
        return pick(random, HIT);
    }

    public static String stand(Random random) {
        return pick(random, STAND);
    }

    public static String blackjack(Random random) {
        return pick(random, BLACKJACK);
    }

    public static String bustPlayer(Random random) {
        return pick(random, BUST_PLAYER);
    }

    public static String bustDealer(Random random) {
        return pick(random, BUST_DEALER);
    }

    public static String win(Random random) {
        return pick(random, WIN);
    }

    public static String lose(Random random) {
        return pick(random, LOSE);
    }

    public static String tie(Random random) {
        return pick(random, TIE);
    }
}
