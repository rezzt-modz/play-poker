playsound minecraft:ui.stonecutter.take_result player @a ~ ~ ~ 1 1.5
particle dust{color:[1.000,0.259,0.259],scale:1} ~ ~ ~ 0.05 0 0.05 0 3 normal
tag @s remove 777.ready
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "stand", cmd: "tag @s remove 777.active"}
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "hit", cmd: "tag @s remove 777.active"}