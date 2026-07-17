scoreboard players reset @s 777.blackjack.hand_value
scoreboard players reset @s 777.blackjack.aces_in_hand
scoreboard players reset @s 777.blackjack.aces_soft

tag @s add 777.get_value
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "card_slot", cmd: "function 777:blackjack/card/get_value"}
tag @s remove 777.get_value

# after ANY add, repair busts by downgrading 11 to 1
function 777:blackjack/bet/soft_fix

execute store result storage 777:blackjack hand_value int 1 run scoreboard players get @s 777.blackjack.hand_value

tag @s add 777.store_num
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "hand_value", cmd: "function 777:blackjack/hand_value/set_text"}
tag @s remove 777.store_num

execute if score @s 777.blackjack.hand_value matches 22.. run function 777:blackjack/bet/bust/start