execute as @e[type=armor_stand,tag=slot_machine,tag=!placed] at @s run function 777:slot_machine/place

execute as @e[type=armor_stand,tag=slot_machine,tag=placed] at @s run function 777:slot_machine/main
execute as @e[type=minecraft:interaction,tag=slot_machine,tag=using] at @s run function 777:slot_machine/spin/main
execute as @e[type=minecraft:interaction,tag=slot_machine,tag=winning] at @s run function 777:slot_machine/rewards/main

# blackjack
execute as @e[type=armor_stand,tag=blackjack_table,tag=!placed] at @s run function 777:blackjack/place
execute as @e[type=armor_stand,tag=blackjack_table,tag=placed] at @s run function 777:blackjack/main