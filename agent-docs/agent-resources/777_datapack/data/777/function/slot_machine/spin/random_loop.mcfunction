execute store result score #rand 777.bool_score run random value 1..3

execute if score #rand 777.bool_score matches 1 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/spin_loop1/tween {to_frame: 0, duration: 2}
execute if score #rand 777.bool_score matches 2 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/spin_loop2/tween {to_frame: 0, duration: 2}
execute if score #rand 777.bool_score matches 3 positioned ~ ~-1 ~ as @n[type=item_display,tag=aj.slot_machine.root] at @s run function animated_java:slot_machine/animations/spin_loop3/tween {to_frame: 0, duration: 2}