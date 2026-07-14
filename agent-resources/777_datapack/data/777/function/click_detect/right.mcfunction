advancement revoke @s only 777:right_click

tag @s add clicked
execute as @e[type=interaction,distance=..8] at @s run function 777:click_detect/find_targetted
tag @s remove clicked