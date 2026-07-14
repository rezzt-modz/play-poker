# step 3
# say picking random card

$data modify entity @s data.selected_card set from entity @s data.deck[$(card_index)]

execute store result entity @s data.deal_to_group int 1 run scoreboard players get @s 777.blackjack.deal_to_group
execute store result entity @s data.deal_round int 1 run scoreboard players get @s 777.blackjack.deal_round

function 777:blackjack/dealer/pick_card/deal_card with entity @s data

execute if score #has_no_bet 777.math_temp matches 1.. run return 0

$data remove entity @s data.deck[$(card_index)]