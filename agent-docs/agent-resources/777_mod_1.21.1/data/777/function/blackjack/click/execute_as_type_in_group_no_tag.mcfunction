tag @s add 777.blackjack_find_temp
$execute as @e[tag=777.blackjack_entity,tag=777.blackjack.$(type)] if score @s 777.blackjack.group = @n[tag=777.blackjack_find_temp] 777.blackjack.group if score @s 777.real_ID = @n[tag=777.blackjack_find_temp] 777.real_ID at @s run $(cmd)
tag @s remove 777.blackjack_find_temp