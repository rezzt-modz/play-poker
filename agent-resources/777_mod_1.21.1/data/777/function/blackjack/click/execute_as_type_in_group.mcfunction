tag @n[tag=blackjack_temp] add 777.blackjack_find_temp
$execute as @e[tag=777.blackjack_entity,tag=777.blackjack.$(type)] if score @s 777.blackjack.group = @n[tag=777.blackjack_find_temp] 777.blackjack.group if score @s 777.real_ID = @n[tag=777.blackjack_find_temp] 777.real_ID at @s run $(cmd)
tag @n[tag=blackjack_temp] remove 777.blackjack_find_temp