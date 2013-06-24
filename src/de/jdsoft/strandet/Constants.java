package de.jdsoft.strandet;


import android.graphics.Color;

public interface Constants {
    static final public int RIVER_COLOR = Color.rgb(49, 58, 92);



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
        0xfff8f8f8, // SNOW
        0xffdddddd, // TUNDRA
        0xffbbbbbb, // BARE
        0xff999999, // SCORCHED
        0xffccd4bb, // TAIGA
        0xffc4ccbb, // SHRUBLAND
        0xffa4c4a8, // TEMPERATE_RAIN_FOREST
        0xffb4c9a9, // TEMPERATE_DECIDUOUS_FOREST
        0xffc4d4aa, // GRASSLAND
        0xffe4e8ca, // TEMPERATE_DESERT
        0xff9cbba9, // TROPICAL_RAIN_FOREST
        0xffa9cca4, // TROPICAL_SEASONAL_FOREST
        0xffe9ddc7  // SUBTROPICAL_DESERT
    };

    static final int BEACH_COLOR = 0xffe2e6c8;
    static final int LAKE_COLOR = 0xff636C8E;
}
