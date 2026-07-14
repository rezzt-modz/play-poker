#execute as @n[tag=clicked] if items entity @s weapon.mainhand #777:cant_gamble run return run tellraw @s {"color":"red","text":"You cannot gamble this!"}
execute as @n[tag=clicked] unless items entity @s weapon.mainhand * run return run tellraw @s {"color":"red","text":"You must be holding an item!"}

execute unless data entity @s item{count:1,id:"minecraft:barrier"} run function 777:blackjack/bet/remove_bet_item
data modify entity @s item set from entity @n[tag=clicked] SelectedItem
item modify entity @n[tag=clicked] weapon.mainhand 777:remove1

data modify entity @s item.count set value 1

playsound minecraft:entity.pig.saddle player @a ~ ~ ~ 1 2
particle minecraft:composter ~ ~-0.02 ~ 0.1 0.1 0.1 0 3

execute on vehicle run tag @s add 777.has_bet