# step 1

execute store result score @s 777.blackjack.cards_in_deck run data get entity @s data.deck
execute store result storage 777:blackjack deck.amount_in_deck int 1 run scoreboard players get @s 777.blackjack.cards_in_deck

# finish hand is dealer, 777.hit_temp is for when the player on far right tries to hit. if this unless check wasnt here, this would run
execute unless entity @s[tag=777.finish_hand] unless entity @e[tag=777.hit_temp] if score @s 777.blackjack.deal_round matches 3 if score @s 777.blackjack.deal_to_group matches 3 run return run function 777:blackjack/dealer/pick_card/stop_dealing

function 777:blackjack/dealer/pick_card/random_number with storage 777:blackjack deck