tag @s add placed
execute at @s positioned 0.0 0 0.0 rotated ~45 0 positioned ^ ^ ^-0.5 align xz facing -0.5 0 -0.5 rotated ~-45 0 positioned as @s run tp @s ~ ~ ~ ~ ~
setblock ~ ~ ~ minecraft:stone_brick_wall
setblock ~ ~1 ~ minecraft:barrier
summon interaction ~ ~1 ~ {width:1.1f,height:1.1f,Tags:["slot_machine"]}
tp @s ~ ~1 ~
execute positioned ~ ~0.01 ~ facing entity @p eyes rotated ~ 0 run function animated_java:slot_machine/summon/default
execute as @n[tag=aj.slot_machine.root] at @s positioned 0.0 0 0.0 rotated ~45 0 positioned ^ ^ ^-0.5 align xz facing -0.5 0 -0.5 rotated ~-45 0 positioned as @s run tp @s ~ ~ ~ ~ ~