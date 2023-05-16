# AntiLogout

Anti logout serverside mod! Provides /afk and disables combat logging.

*When players log out while in combat, their
"body" stays online for specified amount of
time.*

## Permissions

* `antilogout.bypass.combat` - Allows the player to bypass being tagged in combat

### Commands

* `antilogout.command.afk` - Allows the player to use /afk
  * `antilogout.command.afk.time` - Allows the player to set the time for /afk time <time in seconds>
  * `antilogout.command.afk.others` - Allows the player to use /afk on other players
    * `antilogout.command.afk.others.time` - Allows the player to set the time for /afk on other players

### Admin commands

* `antilogout.command.antilogout` - Allows the player to use /antilogout (for config editing)
  * `antilogout.command.antilogout.edit` - Allows the player to edit the config
  * `antilogout.command.antilogout.reload` - Allows the player to reload the config

## Simple showcase

No bailing from combat (*zombie is used as demo, otherwise active only for player combat*).

https://user-images.githubusercontent.com/34912839/213432960-15d54218-8313-4470-868b-10eb78357764.mp4

For easier afk farming that requires player (e.g. not suitable for spawn chunks)

* similar to carpet's `player` command, but a little more limited

https://user-images.githubusercontent.com/34912839/213676495-f3125d24-d42d-4ee9-80d2-55f33d313aae.mp4
