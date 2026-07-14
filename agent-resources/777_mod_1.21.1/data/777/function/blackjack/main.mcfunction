function 777:blackjack/click/execute_as_type_in_table {type: "bet_item", cmd: "function 777:blackjack/bet/main_as_item"}

function 777:blackjack/click/execute_as_type_in_table {type: "stand", cmd: "function 777:blackjack/stand/main"}

function 777:blackjack/click/execute_as_type_in_table {type: "dealer", cmd: "function 777:blackjack/dealer/main"}

execute at @s unless block ~ ~ ~ minecraft:deepslate_brick_wall run return run function 777:blackjack/break
execute at @s unless block ^1 ^ ^ minecraft:deepslate_brick_wall run return run function 777:blackjack/break
execute at @s unless block ^-1 ^ ^ minecraft:deepslate_brick_wall run return run function 777:blackjack/break