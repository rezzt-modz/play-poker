summon text_display ^-0.385 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot1","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^-0.2 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot2","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^-0.01 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot3","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^0.18 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot4","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^0.37 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot5","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^0.76 ^1.13 ^-0.238 {Tags:["777.blackjack_entity","777.blackjack.card_slot6","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}
summon text_display ^0.75 ^1.13 ^-0.1 {Tags:["777.blackjack_entity","777.blackjack.card_slot7","777.blackjack.dealer_card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-13927937}

summon text_display ^ ^1.14 ^-0.12 {line_width:40,Tags:["777.blackjack_entity","777.blackjack.dealer_hand_value","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.4f,0.4f,0.4f]},text:"",background:0}

# transforms
execute as @e[tag=777.blackjack.dealer_card_slot,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run data merge entity @s {transformation:{left_rotation:[0f,2f,0f,1f],right_rotation:[0.75f,0f,0f,1f],translation:[0f,0.6f,0.2f],scale:[0.0f,0.0f,0.0f]}}

# rotations
execute as @e[tag=777.blackjack.dealer_card_slot,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -75
execute as @e[tag=777.blackjack.dealer_hand_value,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -80

execute as @e[tag=777.blackjack_entity,tag=new] run scoreboard players set @s 777.blackjack.group 0
execute as @e[tag=777.blackjack_entity,tag=new] run scoreboard players operation @s 777.real_ID = @n[tag=777.temp] 777.real_ID

tag @e[tag=777.blackjack_entity,tag=!777.blackjack.dealer,tag=new] remove new