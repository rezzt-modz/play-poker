execute store result score #rand_variant 777.bool_score run random value 1..2

execute if score #rand_variant 777.bool_score matches 1 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/variants/default/apply
execute if score #rand_variant 777.bool_score matches 2 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/variants/icons2/apply

scoreboard players set @s 777.variant 0