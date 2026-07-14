summon interaction ^-0.06 ^1 ^0.375 {width:0.125f,height:0.125f,Tags:["777.blackjack.hit","new","777.blackjack_entity"]}
summon interaction ^-0.25 ^1 ^0.375 {width:0.125f,height:0.125f,Tags:["777.blackjack.stand","new","777.blackjack_entity"]}
summon interaction ^0.25 ^1.135 ^0.325 {width:0.235f,height:0.01f,Tags:["777.blackjack.bet","new","777.blackjack_entity"]}
summon interaction ^-0.16 ^1.025 ^0.225 {width:0.1f,height:0.1f,Tags:["777.blackjack.ready","new","777.blackjack_entity"]}

#summon interaction ^-0.06 ^1 ^0.375 {width:0.125f,height:0.125f,Tags:["777.blackjack.hit","new","777.blackjack_entity"],CustomName:{"color":"red","shadow_color":1,"text":"Hit"}}
#summon interaction ^-0.25 ^1 ^0.375 {width:0.125f,height:0.125f,Tags:["777.blackjack.stand","new","777.blackjack_entity"],CustomName:{"color":"yellow","shadow_color":1,"text":"Stand"}}
#summon interaction ^0.25 ^0.9 ^0.325 {width:0.235f,height:0.235f,Tags:["777.blackjack.bet","new","777.blackjack_entity"],CustomName:{"color":"green","shadow_color":1,"text":"Bet"}}
#summon interaction ^-0.16 ^1.025 ^0.225 {width:0.1f,height:0.1f,Tags:["777.blackjack.ready","new","777.blackjack_entity"],CustomName:{"color":"aqua","shadow_color":1,"text":"Ready"}}

# cards
summon text_display ^-0.26 ^1.126 ^0.12 {Tags:["777.blackjack_entity","777.blackjack.card_slot1","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-2082760}
summon text_display ^-0.072 ^1.126 ^0.12 {Tags:["777.blackjack_entity","777.blackjack.card_slot2","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-2082760}
summon text_display ^0.115 ^1.126 ^0.12 {Tags:["777.blackjack_entity","777.blackjack.card_slot3","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-2082760}
summon text_display ^0.3035 ^1.126 ^0.12 {Tags:["777.blackjack_entity","777.blackjack.card_slot4","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.7f,0.7f,0.7f]},text:"",background:-2082760}

# 3 extra
summon text_display ^-0.165 ^1.126 ^-0.1 {Tags:["extra","777.blackjack_entity","777.blackjack.card_slot5","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.5f,0.5f,0.5f]},text:"",background:-2082760}
summon text_display ^0.02 ^1.126 ^-0.1 {Tags:["extra","777.blackjack_entity","777.blackjack.card_slot6","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.5f,0.5f,0.5f]},text:"",background:-2082760}
summon text_display ^0.21 ^1.126 ^-0.1 {Tags:["extra","777.blackjack_entity","777.blackjack.card_slot7","777.blackjack.card_slot","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.5f,0.5f,0.5f]},text:"",background:-2082760}

# hand value
summon text_display ^0.02 ^1.126 ^0.19 {line_width:40,Tags:["777.blackjack_entity","777.blackjack.hand_value","new"],transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[0.3f,0.3f,0.3f]},text:"",background:0}

execute as @e[tag=777.blackjack.hand_value,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -90

# transforms
execute as @e[tag=777.blackjack.card_slot,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run data merge entity @s {transformation:{left_rotation:[0f,2f,0f,1f],right_rotation:[0.75f,0f,0f,1f],translation:[0f,0.6f,0.2f],scale:[0.0f,0.0f,0.0f]}}
execute as @e[tag=777.blackjack.card_slot,tag=extra,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run data merge entity @s {transformation:{left_rotation:[0f,2f,0f,1f],right_rotation:[0.75f,0f,0f,1f],translation:[0f,0.6f,0.2f],scale:[0.0f,0.0f,0.0f]}}

# rotations
execute as @e[tag=777.blackjack.card_slot,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -90
execute as @e[tag=777.blackjack.card_slot,tag=extra,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -15

summon marker ~ ~1 ~ {Tags:["777.blackjack_entity","new"]}

execute at @n[tag=777.blackjack.bet,tag=new] run summon item_display ~ ~ ~ {Tags:["new","777.blackjack.bet_item","777.blackjack_entity"],CustomName:"bet_item",transformation:{left_rotation:[0f,10000f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,-0.0075f],scale:[0.2f,0.2f,0.2f]},item:{id:"minecraft:barrier",count:1,components:{"minecraft:custom_model_data":6902}}}

execute as @e[tag=777.blackjack.bet_item,tag=new] run ride @s mount @n[type=minecraft:interaction,tag=777.blackjack.bet,tag=new]
execute as @e[tag=777.blackjack.bet_item,tag=new] at @s rotated as @n[tag=777.dealer,tag=new] run tp @s ~ ~ ~ ~ -90

$execute as @e[tag=777.blackjack_entity,tag=new] run scoreboard players set @s 777.blackjack.group $(group)

execute as @e[tag=777.blackjack_entity,tag=new] run scoreboard players operation @s 777.real_ID = @n[tag=777.temp] 777.real_ID

tag @e[tag=777.blackjack_entity,tag=!777.blackjack.dealer,tag=new] remove new