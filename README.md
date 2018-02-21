# moreLoots
A sponge plugin custom minecraft loots

This is a Sponge Plugin work with SpongeAPI 5.1,can add loot on Entity Death.

# Feature
Add loot when entity death. Configure file in "config/moreloots.conf", JSON format.

moreloots.conf Example:
```json
{
    "zombie":{
        "minecraft:gunpowder":16,
        "mw:copperingot":16
    },
    "skeleton":{
        "minecraft:stone":4
    }
}
```
"zombie" is entity name,lowercase.There are other options like "skeleton","creeper",etc.

In EntityName node,there are Key/Value list,key is Minecraft Item ID and value is maxium amount.

# How it works

It will listen EntityDeathEvent,and check if entityName is in the list.
If in list and a random result is ture, random pick a item in this entityName.
Drop N of pick item on ground.(1 < N < maxium)
