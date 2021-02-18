/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

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
package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieVillagerPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.VillagerData;
import net.minecraft.server.v1_16_R3.VillagerProfession;
import net.minecraft.server.v1_16_R3.VillagerType;
import net.minecraft.server.v1_16_R3.World;


@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIEVILLAGER)
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet, IEntityVillagerDataHolder{
	
	private static final DataWatcherObject<Boolean> CONVERTING = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<VillagerData> DATA = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.q);
	
	public EntityZombieVillagerPet(World world){
		super(EntityTypes.ZOMBIE_VILLAGER, world);
	}
	
	public EntityZombieVillagerPet(World world, IPet pet){
		super(EntityTypes.ZOMBIE_VILLAGER, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(CONVERTING, false);
		this.datawatcher.register(DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
	}
	
	@Override
	public void setProfession(int i){
		try{
			this.datawatcher.set(DATA, getVillagerData().withProfession((VillagerProfession) VillagerProfession.class.getFields()[i].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setType(int type){
		try{
			this.datawatcher.set(DATA, getVillagerData().withType((VillagerType) VillagerType.class.getFields()[type].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setLevel(int level){
		try{
			this.datawatcher.set(DATA, getVillagerData().withLevel(level));
		}catch(Exception ignored){
		}
	}
	
	public VillagerData getVillagerData(){
		return this.datawatcher.get(DATA);
	}
}