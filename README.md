# Better Party Crackers
This is a trial plugin based of [this document](https://docs.google.com/document/d/1aJMZBo-iByt0O6xmcxGsi-_NlN1uG5jYzN4zBVtU5hc/edit).

## Information
Spigot-API version: 1.18.2<br>
JDK Version: 17<br>
Gradle version: 7.3<br>
Tested on a local server using Paper 1.18.2 (build 274)<br>
Time spent: 6 hours and 30 minutes (according to WakaTime), see the observations section for more details.
![WakaTime dashboard](https://i.imgur.com/loU0Gxe.png)


## Commands
- /bpc give &lt;player&gt; &lt;type&gt; [&lt;amount&gt;] - Gives a specific cracker to a player
- /bpc list - Lists all cracker types
- /bpc info &lt;type&gt; - Shows information about a cracker type
- /bpc reload - Reloads the plugin's configuration

## Permissions
| Permission                 | Description                          |
|----------------------------|--------------------------------------|
| betterpartycrackers.*      | Grants all permissions               |
| betterpartycrackers.give   | Grants permission to use /bpc give   |
| betterpartycrackers.list   | Grants permission to use /bpc list   |
| betterpartycrackers.info   | Grants permission to use /bpc info   |
| betterpartycrackers.reload | Grants permission to use /bpc reload |


## Observations
- Near 1.5 hours were spent in the serialization system, it was possible to shorten the development time by
using a library or manually mapping each field of the PartyCracker class. In spite of this, I opted
for a custom serialization system, because I feel it is easier to work with and will provide
flexibility in the future.
- Although large commits were made in this repository, I usually perform smaller commits with less changes
for better readability.
