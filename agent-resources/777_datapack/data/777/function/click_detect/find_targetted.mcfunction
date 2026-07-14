scoreboard players set #bool 777.bool_score 0
execute on target store result score #bool 777.bool_score if entity @s[tag=clicked]

tag @s add slot_temp
execute if entity @s[tag=slot_machine] if score #bool 777.bool_score matches 1 at @s as @n[tag=clicked] at @s run function 777:slot_machine/gamble/check_item
tag @s remove slot_temp

tag @s add blackjack_temp
execute if entity @s[tag=777.blackjack.hit] if score #bool 777.bool_score matches 1 at @s as @n[tag=clicked] at @s run function 777:blackjack/click/hit
execute if entity @s[tag=777.blackjack.stand] if score #bool 777.bool_score matches 1 at @s as @n[tag=clicked] at @s run function 777:blackjack/click/stand
execute if entity @s[tag=777.blackjack.bet] if score #bool 777.bool_score matches 1 at @s as @n[tag=clicked] at @s run function 777:blackjack/click/bet {mode: "place"}
execute if entity @s[tag=777.blackjack.ready] if score #bool 777.bool_score matches 1 at @s as @n[tag=clicked] at @s run function 777:blackjack/click/ready
tag @s remove blackjack_temp

data remove entity @s interaction