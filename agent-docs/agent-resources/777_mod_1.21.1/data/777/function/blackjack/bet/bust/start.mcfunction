function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "hand_value", cmd: "function 777:blackjack/bet/bust/text"}

tag @s add 777.busted_temp
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "stand", cmd: "function 777:blackjack/stand/start"}
tag @s remove 777.busted_temp

function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "bet", cmd: "function 777:blackjack/bet/finish/lose_to_dealer"}

tag @s add 777.busted

function 777:blackjack/dealer/chat/bust_player