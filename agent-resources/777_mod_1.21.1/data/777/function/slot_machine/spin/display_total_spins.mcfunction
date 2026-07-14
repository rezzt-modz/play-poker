scoreboard players set #10 777.bool_score 10
scoreboard players operation #temp 777.bool_score = @s 777.total_spins
scoreboard players operation #temp 777.bool_score %= #10 777.bool_score
execute if score #temp 777.bool_score matches 0 run tellraw @a[distance=..8] ["",{"text":"Total Spins on this machine:","color":"dark_aqua"},{"text":" ","color":"aqua"},{"score":{"name":"@s","objective":"777.total_spins"},"color":"aqua"}]