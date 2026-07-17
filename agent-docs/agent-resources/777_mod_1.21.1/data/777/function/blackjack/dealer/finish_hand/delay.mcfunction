tag @s add 777.dealer_temp
function 777:blackjack/click/execute_as_type_in_table {type: "bet", cmd: "function 777:blackjack/bet/compare_scores"}
tag @s remove 777.dealer_temp

execute if score @s 777.blackjack.hand_value matches 22.. run function 777:blackjack/dealer/bust/start