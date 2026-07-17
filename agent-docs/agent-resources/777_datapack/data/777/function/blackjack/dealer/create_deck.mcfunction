#say HMPH, creating the deck
data modify entity @s data.deck set value [2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,10,10,10,10,74,74,74,74,81,81,81,81,75,75,75,75,65,65,65,65]

tag @s add 777.dealer_temp
execute as @e[tag=777.blackjack_entity] if score @s 777.real_ID = @n[tag=777.dealer_temp] 777.real_ID run tag @s add 777.in_game
execute as @e[type=armor_stand,tag=blackjack_table] if score @s 777.real_ID = @n[tag=777.dealer_temp] 777.real_ID run tag @s add 777.in_game
tag @s remove 777.dealer_temp

scoreboard players set @s 777.deal_timer 0

# start with group 3
scoreboard players set @s 777.blackjack.deal_to_group 3

# first round of dealing, increment after card is dealt to dealer
scoreboard players set @s 777.blackjack.deal_round 1

tag @s add 777.dealing