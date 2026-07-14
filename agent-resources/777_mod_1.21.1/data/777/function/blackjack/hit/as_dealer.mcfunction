#say hit

scoreboard players operation @s 777.blackjack.deal_to_group = @n[tag=777.hit_temp] 777.blackjack.group
scoreboard players operation @s 777.blackjack.deal_round = @n[tag=777.hit_temp] 777.blackjack.hit_counter
scoreboard players add @s 777.blackjack.deal_round 1

function 777:blackjack/dealer/pick_card/get_cards_in_deck