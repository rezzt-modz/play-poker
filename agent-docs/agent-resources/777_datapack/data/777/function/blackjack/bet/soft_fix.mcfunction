# while total >= 22 and we still have soft aces, subtract 10 and consume one soft ace
execute if score @s 777.blackjack.hand_value matches 22.. if score @s 777.blackjack.aces_soft matches 1.. run scoreboard players remove @s 777.blackjack.hand_value 10
execute if score @s 777.blackjack.hand_value matches 22.. if score @s 777.blackjack.aces_soft matches 1.. run scoreboard players remove @s 777.blackjack.aces_soft 1
execute if score @s 777.blackjack.hand_value matches 22.. if score @s 777.blackjack.aces_soft matches 1.. run function 777:blackjack/bet/soft_fix