# InfoDataPack
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/GoryMoon/InfoDataPack/Java%20CI)

A mod to add tooltip and jei information to items with a datapack

## Usage
You can find examples below, the same examples are available in the `example_datapack` folder

The json text can for example be generated with the help of [minecraftjson.com](https://minecraftjson.com) or [minecraft.tools](https://minecraft.tools/en/json_text.php)

### Tooltip
![Tooltip image](.github/images/tooltip.png)
```json
{
    "items": [
        "minecraft:diamond",
        "minecraft:coal"
    ],
    "tooltips": [
        {"text":"This ", "extra": [{"text":"is an","underlined":true},{"text":" Example ","color":"green"},{"text":"Tooltip","bold":true,"color":"green"}]},
        ["",{"text":"This "},{"text":"is another ","italic":true,"color":"red"},{"text":"way to ","strikethrough":true},{"keybind":"key.attack"},{"text":" do it","bold":true,"underlined":true,"color":"dark_green"}],
        {"translate":"item.minecraft.diamond","bold":true},
        "No special style also works",
        "§4Legacy coloring §aalso works"
    ]
}
```

### JEI
![Tooltip image](.github/images/jei_info.png)
```json
{
    "items": [
        "minecraft:diamond",
        "minecraft:coal"
    ],
    "infos": [
        "§4Only legacy coloring §aand styling §nworks for these",
        "You can still use json text but no colors or styling will show",
        {"translate":"item.minecraft.diamond","bold":true},
        [{"text": "Do this to color translations "},{"text":"§3"},{"translate":"item.minecraft.coal"}]
    ]
}
```
