# AutoBuilder - Meteor Client Addon

A Meteor Client addon for Minecraft 1.21.8 designed for automated building on anarchy servers like 2b2t.

## Features

- **6x6 Build Grid**: Configure custom block patterns using a visual grid
- **Adjustable Placement Speed**: Control how many blocks are placed per tick
- **Air Place**: Toggle placing blocks without support
- **Placement Range**: Adjustable range from 0-7 blocks
- **XYZ Offset**: Position the build pattern relative to your player
- **Block Selection**: Choose which blocks to use for building
- **Render Preview**: Visual preview of where blocks will be placed

## Installation

1. Install [Meteor Client](https://meteorclient.com/) for Minecraft 1.21.8
2. Download the latest release JAR
3. Place the JAR in your `.minecraft/mods` folder
4. Launch Minecraft with Fabric

## Usage

1. Open Meteor Client GUI
2. Navigate to the "AutoBuilder" category
3. Enable the "auto-builder" module
4. Configure the 6x6 grid by toggling squares
5. Adjust placement speed, range, and offsets as needed
6. Hold blocks in your hotbar and let it build

## Building from Source

```bash
./gradlew build
```

The JAR will be in `build/libs/`

## Open Source

This project is open source with no license restrictions. Feel free to use, modify, and distribute as you wish.
