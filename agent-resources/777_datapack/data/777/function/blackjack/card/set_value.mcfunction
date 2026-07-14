execute if score @s 777.blackjack.selected_card matches 2 run scoreboard players set @s 777.blackjack.card_value 2
execute if score @s 777.blackjack.selected_card matches 3 run scoreboard players set @s 777.blackjack.card_value 3
execute if score @s 777.blackjack.selected_card matches 4 run scoreboard players set @s 777.blackjack.card_value 4
execute if score @s 777.blackjack.selected_card matches 5 run scoreboard players set @s 777.blackjack.card_value 5
execute if score @s 777.blackjack.selected_card matches 6 run scoreboard players set @s 777.blackjack.card_value 6
execute if score @s 777.blackjack.selected_card matches 7 run scoreboard players set @s 777.blackjack.card_value 7
execute if score @s 777.blackjack.selected_card matches 8 run scoreboard players set @s 777.blackjack.card_value 8
execute if score @s 777.blackjack.selected_card matches 9 run scoreboard players set @s 777.blackjack.card_value 9
execute if score @s 777.blackjack.selected_card matches 10 run scoreboard players set @s 777.blackjack.card_value 10
# J
execute if score @s 777.blackjack.selected_card matches 74 run scoreboard players set @s 777.blackjack.card_value 10
# Q
execute if score @s 777.blackjack.selected_card matches 81 run scoreboard players set @s 777.blackjack.card_value 10
# K
execute if score @s 777.blackjack.selected_card matches 75 run scoreboard players set @s 777.blackjack.card_value 10
# A
execute if score @s 777.blackjack.selected_card matches 65 run scoreboard players set @s 777.blackjack.card_value -1

function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "bet", cmd: "function 777:blackjack/bet/get_hand_value"}

function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "dealer", cmd: "function 777:blackjack/dealer/get_hand_value"}