execute as @n[tag=777.blackjack.dealer] if entity @s[tag=777.dealing] run return 0
execute unless entity @s[tag=777.in_game] run return 0
execute unless entity @s[tag=777.active] run return 0

execute if score @s 777.blackjack.hit_counter matches 7.. run return run tellraw @a[tag=clicked] {"color":"red","text":"You have the maximum amount of cards!"}

function 777:blackjack/dealer/chat/hit

tag @s add 777.hit_temp
function 777:blackjack/click/execute_as_type_in_table {type: "dealer", cmd: "function 777:blackjack/hit/as_dealer"}
tag @s remove 777.hit_temp

scoreboard players add @s 777.blackjack.hit_counter 1

playsound minecraft:block.copper_bulb.place player @a ~ ~ ~ 1 1.25