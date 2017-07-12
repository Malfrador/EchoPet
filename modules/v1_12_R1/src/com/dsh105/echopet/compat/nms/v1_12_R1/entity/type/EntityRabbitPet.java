package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import org.bukkit.entity.Rabbit;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityRabbitPet;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.ai.PetGoalFollowOwner;

import net.minecraft.server.v1_12_R1.*;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.RABBIT)
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet{

	private static final DataWatcherObject<Integer> TYPE = DataWatcher.a(EntityRabbitPet.class, DataWatcherRegistry.b);
	private boolean onGroundLastTick = false;
	private int delay = 0;// bC

	public EntityRabbitPet(World world){
		super(world);
		this.g = new ControllerJumpRabbit(this);
	}

	public EntityRabbitPet(World world, IPet pet){
		super(world, pet);
		this.g = new ControllerJumpRabbit(this);
	}

	@Override
	public Rabbit.Type getRabbitType(){
		return TypeMapping.fromMagic(this.datawatcher.get(TYPE));
	}

	@Override
	public void setRabbitType(Rabbit.Type type){
		this.datawatcher.set(TYPE, TypeMapping.toMagic(type));
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(TYPE, Integer.valueOf(0));
	}

	@Override
	public void onLive(){
		super.onLive();
		if(this.delay > 0){
			this.delay -= 1;
		}
		if(this.onGround){
			if(!this.onGroundLastTick){
				l(false);
				reset();// dv
			}
			ControllerJumpRabbit jumpController = (ControllerJumpRabbit) this.g;
			if(!jumpController.c()){
				if(this.moveController.b() && this.delay == 0){
					PathEntity pathentity = ((PetGoalFollowOwner) petGoalSelector.getGoal("FollowOwner")).getNavigation().l();// Gets path towards the player.
					// if(pathentity != null && pathentity.e() < pathentity.d()){
					// Vec3D vec3d = pathentity.a(this);
					// a(vec3d.x, vec3d.z);
					// dl();
					// }
					Vec3D vec3d = new Vec3D(this.moveController.d(), this.moveController.e(), this.moveController.f());
					if((pathentity != null) && (pathentity.e() < pathentity.d())){
						vec3d = pathentity.a(this);
					}
					a(vec3d.x, vec3d.z);
					dl();
				}
			}else if(!jumpController.d()){
				// dp();
				((ControllerJumpRabbit) this.g).a(true);
			}
		}
		this.onGroundLastTick = this.onGround;
	}

	protected void cu(){// has movecontroller in it, 4 above datawatcher register. contains 010000000000000002D
		super.cu();
		double d0 = this.moveController.c();
		if(d0 > 0.0D){
			double d1 = this.motX * this.motX + this.motZ * this.motZ;
			if(d1 < 0.010000000000000002D){
				b(0.0F, 0.0F, 1.0F, 0.1F);
			}
		}
		this.world.broadcastEntityEffect(this, (byte) 1);// Does leg jump animation I think
	}

	private void reset(){
		resetDelay();// du method
		((ControllerJumpRabbit) g).a(false);// dt()
	}

	private void resetDelay(){// du()
		if(moveController.c() < 2.2D) delay = 10;
		else delay = 1;
	}

	public void dl(){// Above datawatcher register
		l(true);// Plays ambient sound if true, does super.l(flag);
	}

	public class ControllerJumpRabbit extends ControllerJump{// Copied from EntityRabbit

		private EntityRabbitPet c;
		private boolean d = false;

		public ControllerJumpRabbit(EntityRabbitPet entityrabbit){
			super(entityrabbit);
			this.c = entityrabbit;
		}

		public boolean c(){
			return this.a;
		}

		public boolean d(){
			return this.d;
		}

		public void a(boolean flag){
			this.d = flag;
		}

		public void b(){
			if(this.a){
				this.c.dl();// this is the method above ^
				this.a = false;
			}
		}
	}

	static class TypeMapping{

		private static final int[] NMS_TYPES = new int[Rabbit.Type.values().length];
		private static final Rabbit.Type[] INVERSE = new Rabbit.Type[Rabbit.Type.values().length];
		static{
			set(Rabbit.Type.BROWN, 0);
			set(Rabbit.Type.WHITE, 1);
			set(Rabbit.Type.BLACK, 2);
			set(Rabbit.Type.BLACK_AND_WHITE, 3);
			set(Rabbit.Type.GOLD, 4);
			set(Rabbit.Type.SALT_AND_PEPPER, 5);
			set(Rabbit.Type.THE_KILLER_BUNNY, 99);
		}

		private static void set(Rabbit.Type type, int magicValue){
			NMS_TYPES[type.ordinal()] = magicValue;
			if(magicValue < INVERSE.length){
				INVERSE[magicValue] = type;
			}
		}

		protected static Rabbit.Type fromMagic(int magicValue){
			if(magicValue < INVERSE.length){
				return INVERSE[magicValue];
			}else if(magicValue == 99){ return Rabbit.Type.THE_KILLER_BUNNY; }
			// a default
			return Rabbit.Type.BROWN;
		}

		protected static int toMagic(Rabbit.Type type){
			return NMS_TYPES[type.ordinal()];
		}
	}
}
