#say broke
execute as @n[type=item_display,tag=blackjack_model] run kill @s
summon item ~ ~ ~ {Motion:[0.0,0.3,0.0],Item:{id:"minecraft:armor_stand",count:1,components:{"minecraft:max_stack_size":1,"minecraft:item_name":'{"color":"gold","italic":false,"text":"Blackjack Table"}',"minecraft:lore":['{"color":"green","italic":false,"text":"Can be placed"}','{"color":"gray","italic":false,"text":"Can you beat the dealer?"}'],"minecraft:custom_model_data":6902,"minecraft:entity_data":{id:"minecraft:armor_stand",NoGravity:1b,Invulnerable:1b,Small:1b,Invisible:1b,NoBasePlate:1b,Tags:["blackjack_table"],DisabledSlots:4144959}}}}
execute at @s run setblock ~ ~ ~ minecraft:air
execute at @s positioned ^1 ^ ^ if block ~ ~ ~ minecraft:deepslate_brick_wall run setblock ~ ~ ~ minecraft:air
execute at @s positioned ^-1 ^ ^ if block ~ ~ ~ minecraft:deepslate_brick_wall run setblock ~ ~ ~ minecraft:air
kill @e[type=item,nbt={Item:{id:"minecraft:deepslate_brick_wall"}}]

tag @s add 777.dying
execute unless entity @s[tag=777.in_game] as @e[type=item_display,tag=777.blackjack.bet_item] if score @s 777.real_ID = @n[tag=777.dying] 777.real_ID at @s run function 777:blackjack/bet/remove_bet_item
execute if entity @s[tag=777.in_game] run tellraw @a[distance=..8] {"color":"red","italic":true,"text":"Looks like The Dealer took your bets to the grave... Better not break the table while in a game..."}

execute as @e[tag=777.dealer] if score @s 777.real_ID = @n[tag=777.dying] 777.real_ID run kill @s
execute as @e[tag=777.blackjack_entity] if score @s 777.real_ID = @n[tag=777.dying] 777.real_ID run kill @s

kill @s