/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.util.menu.SelectorIcon;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;


public class ConfigOptions extends Options {
	
	public static ConfigOptions instance;
	
	public ConfigOptions(YAMLConfig config){
		super(config);
		instance = this;
		SelectorLayout.loadLayout();
	}
	
	public boolean allowPetType(PetType petType){
		return this.config.getBoolean("pets." + petType.getConfigKeyName() + ".enable", true);
	}
	
	public boolean allowRidersFor(PetType petType){
		/*if(petType == PetType.ENDERDRAGON){
			return false;
		}*/
		return this.config.getBoolean("pets." + petType.getConfigKeyName() + ".allow.riders", true);
	}
	
	public boolean allowData(PetType type, PetData data){
		return this.config.getBoolean("pets." + type.getConfigKeyName() + ".allow." + data.getConfigKeyName(), true);
	}
	
	public boolean forceData(PetType type, PetData data){
		return this.config.getBoolean("pets." + type.getConfigKeyName() + ".force." + data.getConfigKeyName(), false);
	}
	
	public boolean canFly(PetType petType){
		return this.config.getBoolean("pets." + petType.getConfigKeyName() + ".canFly", false);
	}
	
	public boolean canIgnoreFallDamage(PetType petType){
		return this.config.getBoolean("pets." + petType.getConfigKeyName() + ".ignoreFallDamage", true);
	}
	
	public String getCommandString(){
		return this.config.getString("commandString", "pet");
	}
	
	public float getRideSpeed(PetType petType){
		return (float) this.config.getDouble("pets." + petType.getConfigKeyName() + ".rideSpeed", 0.2D);
	}
	
	public float getFlySpeed(PetType petType){
		return (float) this.config.getDouble("pets." + petType.getConfigKeyName() + ".flySpeed", 0.5D);
	}
	
	public double getRideJumpHeight(PetType petType){
		return this.config.getDouble("pets." + petType.getConfigKeyName() + ".rideJump", 0.6D);
	}
	
	public boolean useSql(){
		return this.config.getBoolean("sql.use", false);
	}
	
	public boolean sqlOverride(){
		if(useSql()){
			return this.config.getBoolean("sql.overrideFile");
		}
		return false;
	}
	
	@Override
	public void setDefaults(){
		set("commandString", "pet");
		
		set("autoUpdate", false, "If set to true, EchoPet will automatically download and install", "new updates.");
		set("checkForUpdates", false, "If -autoUpdate- is set to false, EchoPet will notify certain", "players of new updates if they are available (if set to true).");
		
		set("sql.overrideFile", true, "If true, Pets saved to a MySQL Database will override", "those saved to a file (Default and AutoSave Pets)");
		set("sql.use", false);
		set("sql.host", "localhost");
		set("sql.port", 3306);
		set("sql.database", "EchoPet");
		set("sql.username", "none");
		set("sql.password", "none");
		
		set("petNames.My Pet", "allow");
		set("petNamesRegexMatching", true);
		List<HashMap<String, String>> petNamesRegex = new ArrayList<>();
		Map<String, String> nameRegex = new HashMap<>();
		nameRegex.put(".*administrator.*", "deny");
		set("petNamesRegex", petNamesRegex);
		
		set("stripDiacriticsFromNames", true);
		
		set("enableHumanSkinFixing", true, "Connects to Mojang session servers to attempt to fix human skins");
		set("loadSavedPets", true, "Auto-load pets from last session");
		set("multiworldLoadOverride", true, "When true, if -loadSavedPets-", "is set to false, Pets will", "still be loaded when", "players switch worlds");
		
		set("sendLoadMessage", true, "Send message that pet was loaded if -loadSavedPets- is true");
		set("sendForceMessage", true, "For all data values forced, EchoPet will notify the player", "(if set to true).");
		
		set("worlds." + Bukkit.getWorlds().get(0).getName(), true);
		set("worlds.enableByDefault", true);
		
		if(config.getConfigurationSection("worldguard.regions") == null){
			set("worldguard.regions.echopet", true);
			set("worldguard.regions.allowByDefault", true);
		}
		set("worldguard.regionEnterCheck", true);
		
		if(config.getConfigurationSection("trails") == null){
			//@formatter:off
			String[] trailInfo = {
					"Test is the trail name",
					"particleName - Name of Particle that we display.",
					"canToggle - Ability to toggle the trail using a command",
					"canToggle is used for subtrails. Default - true",
					"subtrails - List of trails which will be active with the parent",
					"interval - Delay in ticks between particle spawns. Default - 0",
					"count - Amount of particles to display. Default - 1",
					"speed - Speed of the particle(or extra data)",
					"x, y, z - Offset from the pets location. Default - 0",
					"x, y, z Offset - Maximum random offset. Default - 0",
					"Everything but particleName is OPTIONAL."
			};
			//@formatter:on
			set("trails.test.particleName", "FLAME", trailInfo);
			set("trails.test.permission", "echopet.pet.trail.test");
			set("trails.test.subtrails", Arrays.asList("test2"));
			set("trails.test.interval", 1);
			set("trails.test.speed", 0);
			set("trails.test.count", 2);
			set("trails.test.speed", 0);
			set("trails.test.x", 0);
			set("trails.test.y", 0);
			set("trails.test.z", 0);
			set("trails.test.xOffset", 0);
			set("trails.test.yOffset", 0);
			set("trails.test.zOffset", 0);
			//
			set("trails.test2.particleName", "WATER_SPLASH");
			set("trails.test2.permission", "echopet.pet.trail.test2");
			set("trails.test2.canToggle", false);
			set("trails.test2.interval", 2);
			set("trails.test2.speed", 1);
			set("trails.test2.count", 5);
			set("trails.test2.y", 3);
		}
		
		set("petSelector.allowDrop", true);
		set("petSelector.showDisabledPets", true);
		set("petSelector.giveOnJoin.enable", false);
		set("petSelector.giveOnJoin.usePerm", false);
		set("petSelector.giveOnJoin.perm", "echopet.selector.join");
		set("petSelector.giveOnJoin.slot", 9);
		set("petSelector.clearInvOnJoin", false);
		set("petSelector.item.name", "&aPets");
		set("petSelector.item.lore", "&7Right click to open");
		set("petSelector.item.material", Material.BONE.name());
		set("petSelector.item.materialData", 0);
		
		boolean loadDefault = this.config.get("petSelector.menu.slots") == null;
		int pageCount = SelectorLayout.getTotalPageCount();
		set("petSelector.menu.pages", pageCount);
		set("petSelector.menu.title", "Pets Page: ");
		if(loadDefault){
			for(int page = 0; page <= pageCount; page++){
				for(SelectorIcon icon : SelectorLayout.getDefaultLayout()){
					if(icon.getPage() == page){
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".command", icon.getCommand());
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".petType", icon.getPetType() == null ? "" : icon.getPetType().toString());
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".material", icon.getMaterial().name());
						if(icon.getPetType() != null){
							set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".entityName", icon.getPetType().getMinecraftName());
						}
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".name", (icon.getName() == null ? "" : icon.getName()).replace(ChatColor.COLOR_CHAR, '&'));
						List<String> lore = new ArrayList<>();
						for(String s : icon.getLore()){
							lore.add(s.replace(ChatColor.COLOR_CHAR, '&'));
						}
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".lore", lore);
					}
				}
			}
		}
		
		for(PetType petType : PetType.values()){
			String configOption = petType.getConfigKeyName();
			set("pets." + configOption + ".enable", true);
			set("pets." + configOption + ".tagVisible", true);
			set("pets." + configOption + ".defaultName", petType.getDefaultName());
			set("pets." + configOption + ".interactMenu", true);
			set("pets." + configOption + ".startFollowDistance", 12);
			set("pets." + configOption + ".stopFollowDistance", 4);
			set("pets." + configOption + ".teleportDistance", 40);

			/*set("pets." + configOption + ".attack.canDamagePlayers", false);
			set("pets." + configOption + ".attack.lockRange", 10);
			set("pets." + configOption + ".attack.ticksBetweenAttacks", 20);
			set("pets." + configOption + ".attack.damage", petType.getAttackDamage());
			set("pets." + configOption + ".attack.loseHealth", false);
			set("pets." + configOption + ".attack.health", petType.getMaxHealth());*/
			
			set("pets." + configOption + ".rideSpeed", 0.2D);
			set("pets." + configOption + ".flySpeed", 0.5D);
			set("pets." + configOption + ".jumpHeight", 0.6D);
			
			set("pets." + configOption + ".ignoreFallDamage", true);
			
			// if(petType != PetType.ENDERDRAGON){
			boolean canFly = (petType == PetType.BAT || petType.equals(PetType.BEE) || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER || petType == PetType.VEX);
			set("pets." + configOption + ".canFly", canFly);
			set("pets." + configOption + ".allow.riders", true);
			// }
			
			for(PetData pd : PetData.values()){
				if(petType.isDataAllowed(pd)){
					set("pets." + configOption + ".allow." + pd.getConfigKeyName(), true);
					set("pets." + configOption + ".force." + pd.getConfigKeyName(), false);
				}
			}
		}
		
		config.saveConfig();
	}
}