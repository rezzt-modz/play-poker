advancement revoke @s only 777:left_click

tag @s add clicked
execute as @e[type=interaction,distance=..8] at @s run function 777:click_detect/find_attacker
tag @s remove clicked