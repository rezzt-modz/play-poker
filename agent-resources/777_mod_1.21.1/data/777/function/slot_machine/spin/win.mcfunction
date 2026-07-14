execute store result score #rand 777.bool_score run random value 1..4

execute if score #rand 777.bool_score matches 1 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/win1/tween {to_frame: 0, duration: 2}
execute if score #rand 777.bool_score matches 2 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/win2/tween {to_frame: 0, duration: 2}
execute if score #rand 777.bool_score matches 3 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/win3/tween {to_frame: 0, duration: 2}
execute if score #rand 777.bool_score matches 4 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/win4/tween {to_frame: 0, duration: 2}

execute if score #rand 777.bool_score matches 1 run scoreboard players set @s 777.win_size 4
execute if score #rand 777.bool_score matches 2 run scoreboard players set @s 777.win_size 1
execute if score #rand 777.bool_score matches 3 run scoreboard players set @s 777.win_size 2
execute if score #rand 777.bool_score matches 4 run scoreboard players set @s 777.win_size 3

scoreboard players set @s 777.odds 1