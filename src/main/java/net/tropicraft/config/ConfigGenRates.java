package net.tropicraft.config;

import modconfig.IConfigCategory;

public class ConfigGenRates implements IConfigCategory {

	public static final int EIH_CHANCE = 50;
	public static final int SHIPWRECK_CHANCE = 200;
	public static final int TALL_FLOWERS_CHANCE = 3;
	public static final int BAMBOO_CHANCE = 2;
	public static final int WATERFALL_AMOUNT = 25;
	public static final int TALL_GRASS_CHANCE = 4;
	public static final int CURVED_PALM_CHANCE = 3;
	public static final int LARGE_PALM_CHANCE = 3;
	public static final int NORMAL_PALM_CHANCE = 3;
	
	@Override
	public String getConfigFileName() {
		return "Tropicraft_GenRates";
	}

	@Override
	public String getCategory() {
		return "Tropicraft Gen Rates Config";
	}

	@Override
	public void hookUpdatedValues() {
		//apply changes to code here if needed
	}

}
