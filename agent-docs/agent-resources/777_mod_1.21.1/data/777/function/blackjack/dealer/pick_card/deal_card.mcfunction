# step 4
scoreboard players set @s 777.deal_timer 0

scoreboard players remove @s 777.blackjack.deal_to_group 1
execute if score @s 777.blackjack.deal_to_group matches ..-1 run scoreboard players add @s 777.blackjack.deal_round 1
execute if score @s 777.blackjack.deal_to_group matches ..-1 run function 777:blackjack/click/execute_as_type_in_table {type: "hit", cmd: "scoreboard players add @s 777.blackjack.hit_counter 1"}

execute if score @s 777.blackjack.deal_to_group matches ..-1 run scoreboard players set @s 777.blackjack.deal_to_group 3

# $say trying to deal card "$(selected_card)" to 777.blackjack.card_slot$(deal_round) in group $(deal_to_group)

scoreboard players set #has_no_bet 777.math_temp 0

#$say deal card: $(selected_card)

tag @s add 777.dealer_temp
$function 777:blackjack/click/execute_as_type_in_group_number {group: $(deal_to_group), type: "777.blackjack.card_slot$(deal_round)", cmd: "function 777:blackjack/card/set_from_selected {selected_card: '$(selected_card)'}"}
tag @s remove 777.dealer_temp