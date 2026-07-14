scoreboard players add @s 777.win_time 1

execute if score @s 777.win_time matches 30 positioned ~ ~1 ~ run function 777:slot_machine/rewards/find_tier
execute if score @s 777.win_time matches 60 positioned ~ ~1 ~ run function 777:slot_machine/rewards/find_tier
execute if score @s 777.win_time matches 90 positioned ~ ~1 ~ run function 777:slot_machine/rewards/find_tier

particle minecraft:totem_of_undying ~ ~0.25 ~ 0 0 0 1 3 normal @a
particle minecraft:firework ~ ~0.25 ~ 0 0 0 1 1 normal @a
particle minecraft:end_rod ~ ~0.25 ~ 0 0 0 1.25 2 normal @a
particle minecraft:flash ~ ~ ~ 10 10 10 0 1 normal @a

execute if score @s 777.win_time matches 120 run function 777:slot_machine/rewards/end