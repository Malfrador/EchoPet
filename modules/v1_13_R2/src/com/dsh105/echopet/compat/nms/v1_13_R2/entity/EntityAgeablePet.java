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
package com.dsh105.echopet.compat.nms.v1_13_R2.entity;

import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

public abstract class EntityAgeablePet extends EntityPet implements IEntityAgeablePet{
	
	private static final DataWatcherObject<Boolean> BABY = DataWatcher.a(EntityAgeablePet.class, DataWatcherRegistry.i);
	protected int age;
	private boolean ageLocked = true;
	
	public EntityAgeablePet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntityAgeablePet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	public int getAge(){
		return this.datawatcher.get(BABY).booleanValue() ? -1 : this.age;
	}
	
	public void setAge(int i, boolean flag){
		int j = getAge();
		j += i * 20;
		if(j > 0){
			j = 0;
		}
		setAgeRaw(j);
	}
	
	public void setAge(int i){
		setAge(i, false);
	}
	
	public void setAgeRaw(int i){
		this.datawatcher.set(BABY, Boolean.valueOf(i < 0));
		this.age = i;
	}
	
	public boolean isAgeLocked(){
		return ageLocked;
	}
	
	public void setAgeLocked(boolean ageLocked){
		this.ageLocked = ageLocked;
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(BABY, false);
	}
	
	@Override
	public void inactiveTick(){
		super.inactiveTick();
		if(!(this.world.isClientSide || this.ageLocked)){
			int i = this.getAge();
			if(i < 0){
				++i;
				this.setAge(i);
			}else if(i > 0){
				--i;
				this.setAge(i);
			}
		}
	}
	
	public void setBaby(boolean flag){
		this.datawatcher.set(BABY, flag);
	}
	
	@Override
	public boolean isBaby(){
		return this.datawatcher.get(BABY).booleanValue();
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		if(this.isBaby()){
			return SizeCategory.TINY;
		}else{
			return SizeCategory.REGULAR;
		}
	}
}
