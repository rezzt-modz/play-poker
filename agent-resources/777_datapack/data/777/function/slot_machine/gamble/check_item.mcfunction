execute if entity @n[tag=slot_temp,tag=using] run return 0
execute if entity @n[tag=slot_temp,tag=winning] run return 0
execute unless items entity @s weapon.mainhand #777:can_gamble run return run function 777:slot_machine/gamble/fail

scoreboard players reset @s 777.bool_score
execute if items entity @s weapon.mainhand #777:gamble_low run scoreboard players set @n[tag=slot_temp] 777.bool_score 0
execute if items entity @s weapon.mainhand #777:gamble_high run scoreboard players set @n[tag=slot_temp] 777.bool_score 1

effect give @s minecraft:hunger 1 2 true

item modify entity @s weapon.mainhand 777:remove1

execute store result score #rand_sound 777.bool_score run random value 1..4

execute if score #rand_sound 777.bool_score matches 1 run playsound minecraft:777.slot_machine.insert player @a ~ ~ ~ 1 0.8
execute if score #rand_sound 777.bool_score matches 2 run playsound minecraft:777.slot_machine.insert player @a ~ ~ ~ 1 0.9
execute if score #rand_sound 777.bool_score matches 3 run playsound minecraft:777.slot_machine.insert player @a ~ ~ ~ 1 1
execute if score #rand_sound 777.bool_score matches 4 run playsound minecraft:777.slot_machine.insert player @a ~ ~ ~ 1 1.1

execute store result score @s 777.real_ID run data get entity @s UUID[0]
execute store result score @n[tag=slot_temp] 777.real_ID run data get entity @s UUID[0]

execute as @n[tag=slot_temp] at @s positioned ~ ~-1 ~ run function 777:slot_machine/spin/start