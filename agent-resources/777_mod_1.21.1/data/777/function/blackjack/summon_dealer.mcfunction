# execute store result score #random 777.math_temp run random value -1000..1300

# scoreboard players set #random 777.math_temp 1051

execute store result score #profession 777.math_temp run random value 1..15
execute if score #profession 777.math_temp matches 1 run data modify storage 777:blackjack profession set value "none"
execute if score #profession 777.math_temp matches 2 run data modify storage 777:blackjack profession set value "armorer"
execute if score #profession 777.math_temp matches 3 run data modify storage 777:blackjack profession set value "butcher"
execute if score #profession 777.math_temp matches 4 run data modify storage 777:blackjack profession set value "cartographer"
execute if score #profession 777.math_temp matches 5 run data modify storage 777:blackjack profession set value "cleric"
execute if score #profession 777.math_temp matches 6 run data modify storage 777:blackjack profession set value "farmer"
execute if score #profession 777.math_temp matches 7 run data modify storage 777:blackjack profession set value "fisherman"
execute if score #profession 777.math_temp matches 8 run data modify storage 777:blackjack profession set value "fletcher"
execute if score #profession 777.math_temp matches 9 run data modify storage 777:blackjack profession set value "leatherworker"
execute if score #profession 777.math_temp matches 10 run data modify storage 777:blackjack profession set value "librarian"
execute if score #profession 777.math_temp matches 11 run data modify storage 777:blackjack profession set value "mason"
execute if score #profession 777.math_temp matches 12 run data modify storage 777:blackjack profession set value "nitwit"
execute if score #profession 777.math_temp matches 13 run data modify storage 777:blackjack profession set value "shepherd"
execute if score #profession 777.math_temp matches 14 run data modify storage 777:blackjack profession set value "toolsmith"
execute if score #profession 777.math_temp matches 15 run data modify storage 777:blackjack profession set value "weaponsmith"

function 777:blackjack/summon_villager with storage 777:blackjack