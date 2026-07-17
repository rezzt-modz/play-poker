execute if score @s 777.bool_score matches 0 store result score @s 777.odds run random value 1..120
execute if score @s 777.bool_score matches 1 store result score @s 777.odds run random value 1..20

tag @s add 777.decide_temp
execute as @a[tag=777.cheater] if score @s 777.real_ID = @n[tag=777.decide_temp] 777.real_ID as @n[tag=777.decide_temp] store result score @s 777.odds run random value 1..6
tag @s remove 777.decide_temp

execute if score @s 777.odds matches 2.. run tellraw @a[tag=777.cheater] ["",{"score":{"name":"@s","objective":"777.odds"},"color":"gray"},{"text":"/6 (need ","color":"gray"},{"text":"1","color":"green"},{"text":"/6 to win, you cheater)","color":"gray"}]

execute store result score #rand_sound 777.bool_score run random value 1..4

execute if score #rand_sound 777.bool_score matches 1 run playsound minecraft:777.slot_machine.stop player @a ~ ~ ~ 0.75 0.9
execute if score #rand_sound 777.bool_score matches 2 run playsound minecraft:777.slot_machine.stop player @a ~ ~ ~ 0.75 1
execute if score #rand_sound 777.bool_score matches 3 run playsound minecraft:777.slot_machine.stop player @a ~ ~ ~ 0.75 1.1
execute if score #rand_sound 777.bool_score matches 4 run playsound minecraft:777.slot_machine.stop player @a ~ ~ ~ 0.75 1.2

execute if score @s 777.odds matches 1 run function 777:slot_machine/spin/win
execute if score @s 777.odds matches 2.. run function 777:slot_machine/spin/lose