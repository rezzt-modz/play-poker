playsound minecraft:777.slot_machine.jackpot player @a ~ ~ ~ 1 1
tag @s add winning
tag @s remove using

tag @s add 777.win_temp
execute as @a if score @s 777.real_ID = @n[tag=777.win_temp] 777.real_ID run tellraw @a ["",{"selector":"@s","bold":true,"color":"gold"},{"text":" JUST WON BIG AT THE SLOT MACHINE!","bold":true,"color":"yellow"}]
tag @s remove 777.win_temp