# say HMPH, i will finish my hand

scoreboard players set @s 777.deal_timer 0

function 777:blackjack/click/execute_as_type_in_group_number {group: 0, type: "777.blackjack.card_slot2", cmd: "function 777:blackjack/card/reveal_mystery"}

# start with round 2 to get the dealers second card
scoreboard players set @s 777.blackjack.deal_round 3

tag @s add 777.finish_hand

function 777:blackjack/dealer/get_hand_value