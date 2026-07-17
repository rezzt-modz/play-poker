execute if entity @s[tag=777.dealing] run function 777:blackjack/dealer/deal

execute if entity @s[tag=777.finish_hand] run function 777:blackjack/dealer/finish_hand/main

execute if score @s 777.blackjack.reset_timer matches 1.. run scoreboard players remove @s 777.blackjack.reset_timer 1
execute if score @s 777.blackjack.reset_timer matches 40 run function 777:blackjack/dealer/finish_hand/delay
execute if score @s 777.blackjack.reset_timer matches 11 run function 777:blackjack/reset_flip
execute if score @s 777.blackjack.reset_timer matches 1 run function 777:blackjack/reset_table