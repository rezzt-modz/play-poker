# higher is better (4 is best loot)

execute store result score #rand_item 777.bool_score run random value 1..7

execute if score #rand_item 777.bool_score matches 1 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:diamond",count:64}}
execute if score #rand_item 777.bool_score matches 2 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:netherite_ingot",count:8}}
execute if score #rand_item 777.bool_score matches 3 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:elytra",count:1,components:{"minecraft:damage":432}}}
execute if score #rand_item 777.bool_score matches 4 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:iron_block",count:32}}
execute if score #rand_item 777.bool_score matches 5 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:gold_block",count:32}}
execute if score #rand_item 777.bool_score matches 6 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:enchanted_golden_apple",count:2}}
execute if score #rand_item 777.bool_score matches 7 run summon item ~ ~ ~ {Motion:[0.0,0.5,0.0],Item:{id:"minecraft:totem_of_undying",count:3,components:{"minecraft:max_stack_size":3}}}