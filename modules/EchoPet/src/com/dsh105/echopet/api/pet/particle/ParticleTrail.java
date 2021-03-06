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
package com.dsh105.echopet.api.pet.particle;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.particle.Trail;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @since Jul 11, 2016
 */
public class ParticleTrail implements Trail{
	
	private final Particle particle;
	private final String name, particleType, permission;
	private boolean canToggle;
	private int tickDelay;
	private float speed;
	private int count;
	private double x, y, z;
	private float xOffset, yOffset, zOffset;
	private Trail parentTrail;
	private Collection<String> subTrailNames;
	private Set<Trail> subTrails;
	// private BukkitTask runnable;
	private Map<Integer, Set<Trail>> trailDelays = new HashMap<>();
	private Map<Integer, BukkitTask> trailRunnables = new HashMap<>();
	
	public ParticleTrail(String name, String particleType, String permission, boolean canToggle, Collection<String> subTrailNames, int tickDelay, float speed, int count, double x, double y, double z, float xOffset, float yOffset, float zOffset){
		this.subTrails = new HashSet<>();
		this.name = name;
		this.particle = Particle.valueOf(particleType);
		this.particleType = particleType;
		this.permission = permission;
		this.canToggle = canToggle;
		this.subTrailNames = subTrailNames;
		this.tickDelay = tickDelay;
		this.speed = speed;
		this.count = count;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public String getParticleType(){
		return particleType;
	}
	
	@Override
	public String getPermission(){
		return permission;
	}
	
	@Override
	public boolean canToggle(){
		return canToggle;
	}
	
	@Override
	public int getTickDelay(){
		return tickDelay;
	}
	
	@Override
	public float getSpeed(){
		return speed;
	}
	
	@Override
	public int getCount(){
		return count;
	}
	
	@Override
	public double getX(){
		return x;
	}
	
	@Override
	public double getY(){
		return y;
	}
	
	@Override
	public double getZ(){
		return z;
	}
	
	@Override
	public float getXOffset(){
		return xOffset;
	}
	
	@Override
	public float getYOffset(){
		return yOffset;
	}
	
	@Override
	public float getZOffset(){
		return zOffset;
	}
	
	@Override
	public Trail getParentTrail(){
		return parentTrail;
	}
	
	@Override
	public void setParentTrail(Trail parentTrail){
		this.parentTrail = parentTrail;
	}
	
	@Override
	public Collection<String> getSubTrailNames(){
		return subTrailNames;
	}
	
	@Override
	public Set<Trail> getSubTrails(){
		return subTrails;
	}
	
	@Override
	public void addSubTrail(Trail subTrail){
		subTrails.add(subTrail);
	}
	
	@Override
	public void start(final IPet pet){
		cancel();
		
		collectTrails(this);
		
		for(int delay : trailDelays.keySet()){
			final Set<Trail> trails = trailDelays.get(delay);
			trailRunnables.put(delay, new BukkitRunnable(){
				
				public void run(){
					if(pet == null || pet.getOwner() == null || pet.getCraftPet() == null || pet.getCraftPet().getLocation() == null){
						cancel();
						return;
					}
					for(Trail trail : trails){
						trail.displayTrail(pet);
					}
				}
			}.runTaskTimer(EchoPet.getPlugin(), delay, delay));
		}
	}
	
	private void collectTrails(Trail trail){
		Set<Trail> trails = trailDelays.get(trail.getTickDelay());
		if(trails == null) trails = new HashSet<>();
		trails.add(trail);
		trailDelays.put(trail.getTickDelay(), trails);
		for(Trail t : trail.getSubTrails()){
			collectTrails(t);
		}
	}
	
	@Override
	public void cancel(){
		for(BukkitTask runnable : trailRunnables.values()){
			runnable.cancel();
		}
		trailDelays.clear();
		trailRunnables.clear();
		for(Trail trail : subTrails){
			trail.cancel();
		}
	}
	
	@Override
	public void displayTrail(final IPet pet){
		pet.getLocation().getWorld().spawnParticle(particle, pet.getLocation().add(x, y, z), count, xOffset, yOffset, zOffset, speed);
		// ParticleEffect.fromName(particleType).display(xOffset, yOffset, zOffset, speed, count, pet.getLocation().add(x, y, z), 256D);
	}
	
	@Override
	public ParticleTrail clone(){
		// We can clone the subTrailNames because it should never be modified after loading.
		ParticleTrail clone = new ParticleTrail(name, particleType, permission, canToggle, subTrailNames, tickDelay, speed, count, x, y, z, xOffset, yOffset, zOffset);
		collectSubTrailClones(this, clone);
		return clone;
	}
	
	private void collectSubTrailClones(Trail parentTrail, Trail parentTrailCloned){
		Set<Trail> subTrailClone = new HashSet<>();
		for(Trail trail : parentTrail.getSubTrails()){
			Trail trailClone = trail.clone();
			trailClone.setParentTrail(parentTrailCloned);
			collectSubTrailClones(trail, trailClone);
			subTrailClone.add(trailClone);
		}
		parentTrailCloned.getSubTrails().addAll(subTrailClone);
	}
}
