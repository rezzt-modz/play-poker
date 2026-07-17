execute as @n[tag=777.blackjack.dealer] if entity @s[tag=777.dealing] run return 0
execute unless entity @s[tag=777.in_game] run return 0
execute unless entity @s[tag=777.active] run return 0
execute if entity @s[tag=777.standing] run return run tellraw @a[tag=clicked] {"color":"yellow","italic":true,"text":"Already standing. Once you stand, you cannot take it back!"}
tag @s add 777.standing

scoreboard players set #invalid 777.math_temp 0

tag @s add 777.final_test
# this will run if there is group that is not standing
execute as @e[tag=777.blackjack.stand] if score @s 777.real_ID = @n[tag=777.final_test] 777.real_ID if entity @s[tag=777.active,tag=!777.standing] run scoreboard players set #invalid 777.math_temp 1
tag @s remove 777.final_test

playsound minecraft:block.note_block.pling player @a ~ ~ ~ 0.5 2

execute unless entity @e[tag=777.busted_temp] run function 777:blackjack/dealer/chat/stand

function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "hit", cmd: "tag @s remove 777.active"}

execute if score #invalid 777.math_temp matches 1 run return run function 777:blackjack/stand/waiting_for_others_to_stand

# say All players stood, dealer will finish his hand now
function 777:blackjack/click/execute_as_type_in_table {type: "dealer", cmd: "function 777:blackjack/dealer/finish_hand/start"}