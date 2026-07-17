execute if data entity @s item{count:1,id:"minecraft:barrier"} run return 0

summon item ~ ~0.2 ~ {PickupDelay:2,Motion:[0.0,0.2,0.0],Tags:["new"],Item:{id:"minecraft:stone",count:1}}
data modify entity @n[type=item,tag=new] Item set from entity @s item
tag @n[type=item,tag=new] remove new

summon item ~ ~0.2 ~ {PickupDelay:2,Motion:[0.0,0.2,0.0],Tags:["new"],Item:{id:"minecraft:stone",count:1}}
data modify entity @n[type=item,tag=new] Item set from entity @s item
tag @n[type=item,tag=new] remove new
data merge entity @s {item:{id:"minecraft:barrier",count:1,components:{"minecraft:custom_model_data":6903}}}

playsound minecraft:entity.item.pickup player @a ~ ~ ~ 1 0.7
particle minecraft:white_smoke ~ ~0.02 ~ 0 0 0 0.025 3

tag @s remove 777.ready

# say Rewarded!