# step 2
#pick random card from deck

$execute store result storage 777:blackjack deck.card_index int 1 run random value 0..$(amount_in_deck)

function 777:blackjack/dealer/pick_card/pick_random_card with storage 777:blackjack deck