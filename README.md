<div align="center">

# CookiePortal

**Packet-only 3D see-through Nether and End portals for Paper and Folia.**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21--1.26.3-16a34a?style=flat-square)](https://papermc.io)
[![Java](https://img.shields.io/badge/Java-21%20%7C%2025-ea580c?style=flat-square)](https://adoptium.net)
[![PacketEvents](https://img.shields.io/badge/PacketEvents-2.11%E2%80%932.14-7c3aed?style=flat-square)](https://modrinth.com/plugin/packetevents)
[![Version](https://img.shields.io/badge/Version-1.4.0-334155?style=flat-square)](https://github.com/KhoaSuperT1/CookiePortal)

[Support Discord](https://discord.gg/NZycRMWrSS) · [MIT License](LICENSE)

</div>

---

> [!NOTE]
> **Fork Disclaimer & AI Notice:**
> CookiePortal is a refactored, leak-fixed fork derived from legacy software-rasterization code. Author **Cookie (LoveCookieee)** does not claim original ownership of the underlying portal preview concept.
> Roughly **80%** of the code and `1.21 – 1.26.3` updates in this fork were written with AI assistance. Please test it on a staging server first—especially bandwidth limits (`update-interval-ticks` and `max-block-changes-per-update`) for larger player counts.

## How It Works

When a player with `portal.see` walks near a Nether or End portal, CookiePortal computes their line-of-sight frustum through the portal opening, captures an async destination chunk snapshot, and streams client-only block changes and projected entities over PacketEvents. Players without permission continue to see standard vanilla portal blocks.

```text
Viewer Eye Raycast -> Portal Opening Frustum -> Async Destination Snapshot
        -> Sealed Virtual Room Mask -> PacketEvents Block & Entity Stream
```

### Changes in v1.4.0

- Updated and verified for **Minecraft 1.21 – 1.26.3** (Java 21 bytecode, compiled against both Paper `1.21.4` and `26.3`).
- Replaced direct `GameRule.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY` access with a registry/reflective resolver (`Registry.GAME_RULE` / `GameRules` + legacy fallback) for instant portal travel on Paper 26.x.
- Added safe reflection/fallback bridging between `Player#sendMultiBlockChange` and `Player#sendBlockChanges` across Paper 1.21 and 26.3.
- Updated PacketEvents entity projection support for **PacketEvents 2.14.0** (compatible with `2.11.0+`).

## Features

- **Per-Player Packet Streams:** Block and entity packets are sent per player channel, isolating viewers on opposite sides of a portal.
- **Sealed Virtual Room Shell:** Renders a bounded `28 x 28 x 20` view framed with dimension-appropriate walls (`BLACKSTONE`/`NETHERRACK`, `END_STONE`, `STONE`/`DEEPSLATE`).
- **Entity Projection:** Projects nearby destination mobs/players into the window while hiding local entities behind the frame.
- **Biome Atmosphere:** Spawns destination-appropriate particles (Warped/Crimson spores, Soul fire/particles, Basalt ash, End rods, Overworld clouds).
- **End Portal Parallax & Sky Arrival:** Static End diorama effect below End portals, plus an optional sky-arrival descent when entering the End.
- **Optional Dimension Stacking:** Disabled by default; supports vertical dimension linking (`END -> OVERWORLD -> NETHER`) with optional bedrock openings.
- **Folia Ready:** Built around region/entity schedulers and concurrent caches.

## Requirements

| Requirement | Version |
| :--- | :--- |
| Server Platform | Paper, Purpur, Leaf, or Folia (`1.21 – 1.26.3`) |
| Java Runtime | Java 21+ (Java 25 for `26.1 – 26.3`) |
| Required Plugin | [PacketEvents](https://modrinth.com/plugin/packetevents) (`2.11.0+` for 1.21.x, `2.14.0+` for 26.x) |

## Configuration

<details>
<summary>Default <code>config.yml</code></summary>

```yaml
nether: enable
the_end: enable

render:
  enabled: true
  activation-distance: 20
  update-interval-ticks: 2
  room-width: 28
  room-height: 28
  room-depth: 20
  max-block-changes-per-update: 10000
  look-dot-threshold: 0.35
  restore-vanilla-surface-distance: 20
  source-refresh-ticks: 40

animation:
  enabled: true
  duration-ticks: 24

entities:
  enabled: true
  maximum-per-portal: 8

debug: false

portal:
  match-destination-size: true

end-portal:
  enabled: true
  preview:
    enabled: true
    activation-distance: 12
    update-interval-ticks: 2
    depth: 9
    particle-minimum-distance: 7
  sky-arrival:
    enabled: false
    height-above-ground: 52
    hover-ticks: 40
    descent-speed: 0.42
    fall-protection-ticks: 80

dimension-stacking:
  enabled: false
  transition-cooldown-millis: 3000
  worlds:
    overworld: ""
    nether: ""
  holes:
    enabled: false
    chance-per-chunk: 0.18
    radius: 1
  nether-arrival-y: 102
  overworld-arrival-y: -50
  end-drop-height: 356
```

</details>

## Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/cookieportal info` | Show registered portals, active views, and dimension-stack state | `cookieportal.admin` |
| `/cookieportal reload` | Reload `config.yml` and restart portal renderers | `cookieportal.admin` |

Alias: `/cp`

| Permission | Description | Default |
| :--- | :--- | :---: |
| `portal.see` / `cookieportal.see` | Allow viewing the 3D portal preview | `op` |
| `cookieportal.admin` | Access `/cookieportal` commands | `op` |

## Building from Source

Requires **Java 21+** (Java 25 for the `verify-26.3` profile) and **Apache Maven 3.9+**.

```bash
git clone https://github.com/KhoaSuperT1/CookiePortal.git
cd CookiePortal
mvn package
```

Compiled artifact: `target/CookiePortal-1.4.0.jar`

## Support

- **Discord:** https://discord.gg/NZycRMWrSS

---

<div align="center"><sub>Made with love and care by <b>Java-Cookieee</b> &amp; AI.</sub></div>
