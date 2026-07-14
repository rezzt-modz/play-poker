execute if score @s 777.blackjack.hand_value matches 17.. run function 777:blackjack/dealer/finish_hand/end

# only deal to group zero
scoreboard players set @s 777.blackjack.deal_to_group 0

scoreboard players add @s 777.deal_timer 1
execute if score @s 777.deal_timer matches 12.. run function 777:blackjack/dealer/pick_card/get_cards_in_deck