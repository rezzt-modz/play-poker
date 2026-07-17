function 777:blackjack/click/execute_as_type_in_table {type: "dealer_card_slot", cmd: "function 777:blackjack/dealer/bust/text"}
tellraw @a[distance=..3] [{"color":"yellow","text":"Dealer busted with "},{"bold":true,"color":"gold","score":{"name":"@s","objective":"777.blackjack.hand_value"}},{"bold":false,"color":"yellow","text":"!"}]

function 777:blackjack/dealer/chat/bust_dealer