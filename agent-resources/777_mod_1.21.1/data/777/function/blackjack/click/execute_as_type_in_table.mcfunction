tag @s add 777.blackjack_find_table_temp
$execute as @e[tag=777.blackjack_entity,tag=777.blackjack.$(type),distance=..5] if score @s 777.real_ID = @n[tag=777.blackjack_find_table_temp] 777.real_ID at @s run $(cmd)
tag @s remove 777.blackjack_find_table_temp