say placed blackjack
execute at @s positioned 0.0 0 0.0 rotated ~45 0 positioned ^ ^ ^-0.5 align xz facing -0.5 0 -0.5 rotated ~-45 0 positioned as @s run tp @s ~ ~ ~ ~ ~

execute at @s unless block ^1 ^ ^ air run return run function 777:blackjack/break
execute at @s unless block ^-1 ^ ^ air run return run function 777:blackjack/break

execute store result score @s 777.real_ID run data get entity @s UUID[0]

tag @s add placed
execute at @s run setblock ~ ~ ~ minecraft:deepslate_brick_wall
execute at @s run setblock ^1 ^ ^ minecraft:deepslate_brick_wall
execute at @s run setblock ^-1 ^ ^ minecraft:deepslate_brick_wall

execute at @s run function 777:blackjack/summon_dealer

scoreboard players operation @n[tag=777.dealer,tag=new] 777.real_ID = @s 777.real_ID
scoreboard players operation @n[tag=777.dealer_visual,tag=new] 777.real_ID = @s 777.real_ID

tag @s add 777.temp
execute as @n[tag=777.dealer,tag=new] at @s rotated as @n[tag=777.temp] run tp @s ~ ~ ~ ~ ~
execute as @n[tag=777.dealer_visual,tag=new] at @s rotated as @n[tag=777.temp] run tp @s ~ ~ ~ ~ ~

execute at @s positioned ^ ^ ^ run function 777:blackjack/summon_interactions {group: 2}
execute at @s positioned ^0.94 ^ ^ run function 777:blackjack/summon_interactions {group: 3}
execute at @s positioned ^-1.005 ^ ^ run function 777:blackjack/summon_interactions {group: 1}

execute at @s positioned ^ ^ ^ run function 777:blackjack/place_dealer_cards

summon item_display ~ ~0.5 ~ {teleport_duration:0,Tags:["blackjack_model","new"],item:{id:"minecraft:armor_stand",count:1,components:{"minecraft:custom_model_data":6902}}}
execute as @n[type=item_display,tag=blackjack_model,tag=new] at @s rotated as @n[tag=777.temp] positioned 0.0 0 0.0 rotated ~45 0 positioned ^ ^ ^-0.5 align xz facing -0.5 0 -0.5 rotated ~-45 0 positioned as @s run tp @s ~ ~ ~ ~ ~
scoreboard players operation @n[type=item_display,tag=blackjack_model,tag=new] 777.real_ID = @s 777.real_ID
tag @n[type=item_display,tag=blackjack_model,tag=new] remove new

tag @s remove 777.temp

tag @n[tag=777.dealer,tag=new] remove new
tag @n[tag=777.dealer_visual,tag=new] remove new