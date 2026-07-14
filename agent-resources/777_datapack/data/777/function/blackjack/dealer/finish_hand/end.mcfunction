tag @s remove 777.finish_hand

scoreboard players set @s 777.blackjack.reset_timer 70

# say ended hand

#tag @s add 777.dealer_temp
#function 777:blackjack/click/execute_as_type_in_table {type: "bet", cmd: "function 777:blackjack/bet/compare_scores"}
#tag @s remove 777.dealer_temp