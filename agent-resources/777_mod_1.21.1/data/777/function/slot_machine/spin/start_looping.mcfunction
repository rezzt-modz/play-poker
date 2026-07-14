execute positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/spin_start/play

execute store result score #rand_sound 777.bool_score run random value 1..4

execute if score #rand_sound 777.bool_score matches 1 run playsound minecraft:777.slot_machine.spinning player @a ~ ~ ~ 1 0.9
execute if score #rand_sound 777.bool_score matches 2 run playsound minecraft:777.slot_machine.spinning player @a ~ ~ ~ 1 1
execute if score #rand_sound 777.bool_score matches 3 run playsound minecraft:777.slot_machine.spinning player @a ~ ~ ~ 1 1.1
execute if score #rand_sound 777.bool_score matches 4 run playsound minecraft:777.slot_machine.spinning player @a ~ ~ ~ 1 1.2