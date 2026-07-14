tag @s add 777.bet_check
# this will run if there is a bet with an item
execute as @e[type=item_display,tag=777.blackjack.bet_item] if score @s 777.real_ID = @n[tag=777.bet_check] 777.real_ID unless data entity @s item{count:1,id:"minecraft:barrier"} at @s run scoreboard players set #invalid 777.math_temp 1
tag @s remove 777.bet_check