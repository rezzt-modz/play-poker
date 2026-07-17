# say getting value and adding to @n[tag=777.get_value]

# non ace
execute unless score @s 777.blackjack.card_value matches -1 run scoreboard players operation @n[tag=777.get_value] 777.blackjack.hand_value += @s 777.blackjack.card_value

# if ace add 11, track counts
execute if score @s 777.blackjack.card_value matches -1 run scoreboard players add @n[tag=777.get_value] 777.blackjack.hand_value 11
execute if score @s 777.blackjack.card_value matches -1 run scoreboard players add @n[tag=777.get_value] 777.blackjack.aces_in_hand 1
execute if score @s 777.blackjack.card_value matches -1 run scoreboard players add @n[tag=777.get_value] 777.blackjack.aces_soft 1
