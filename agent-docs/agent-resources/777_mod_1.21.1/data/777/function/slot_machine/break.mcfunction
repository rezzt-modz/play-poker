execute as @n[type=item_display,tag=aj.slot_machine.root] run function animated_java:slot_machine/remove/this
summon item ~ ~-1 ~ {Motion:[0.0,0.3,0.0],Item:{id:"minecraft:armor_stand",count:1,components:{"minecraft:max_stack_size":1,"minecraft:item_name":'{"color":"gold","italic":false,"text":"Slot Machine"}',"minecraft:lore":['{"color":"green","italic":false,"text":"Can be placed"}','{"color":"gray","italic":false,"text":"Time to get rich!"}'],"minecraft:custom_model_data":6901,"minecraft:entity_data":{id:"minecraft:armor_stand",NoGravity:1b,Invulnerable:1b,Small:1b,Invisible:1b,NoBasePlate:1b,Tags:["slot_machine"],DisabledSlots:4144959}}}}
setblock ~ ~ ~ air
kill @n[type=interaction,tag=slot_machine]
kill @s