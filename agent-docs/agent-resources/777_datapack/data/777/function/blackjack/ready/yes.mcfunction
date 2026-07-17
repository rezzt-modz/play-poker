playsound minecraft:block.note_block.harp player @a ~ ~ ~ 1 2
particle dust{color:[0.153,1.000,0.153],scale:1} ~ ~ ~ 0.05 0 0.05 0 3 normal
tellraw @a[tag=clicked] {"color":"aqua","text":"Bet confirmed!"}
tag @s add 777.ready
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "stand", cmd: "tag @s add 777.active"}
function 777:blackjack/click/execute_as_type_in_group_no_tag {type: "hit", cmd: "tag @s add 777.active"}