package de.jdsoft.stranded.map;


import android.graphics.Color;

public interface Constants {
    static final public int RIVER_COLOR = 0x313A5Cff; //Color.rgb(49, 58, 92);


    // Biome
    static final int SNOW = 0;
    static final int TUNDRA = 1;
    static final int BARE = 2;
    static final int SCORCHED = 3;
    static final int TAIGA = 4;
    static final int SHRUBLAND = 5;
    static final int TEMPERATE_RAIN_FOREST = 6;
    static final int TEMPERATE_DECIDUOUS_FOREST = 7;
    static final int GRASSLAND = 8;
    static final int TEMPERATE_DESERT = 9;
    static final int TROPICAL_RAIN_FOREST = 10;
    static final int TROPICAL_SEASONAL_FOREST = 11;
    static final int SUBTROPICAL_DESERT = 12;

    // Biome color
    static final int[] BIOME_COLOR = {
        0xf8f8f8ff, // SNOW
        0xddddddff, // TUNDRA
        0xbbbbbbff, // BARE
        0x999999ff, // SCORCHED
        0xccd4bbff, // TAIGA
        0xc4ccbbff, // SHRUBLAND
        0xa4c4a8ff, // TEMPERATE_RAIN_FOREST
        0xb4c9a9ff, // TEMPERATE_DECIDUOUS_FOREST
        0xc4d4aaff, // GRASSLAND
        0xe4e8caff, // TEMPERATE_DESERT
        0x9cbba9ff, // TROPICAL_RAIN_FOREST
        0xa9cca4ff, // TROPICAL_SEASONAL_FOREST
        0xe9ddc7ff  // SUBTROPICAL_DESERT
    };

    static final int BEACH_COLOR = 0xe2e6c8ff;
    static final int LAKE_COLOR = 0x636C8Eff;
}
