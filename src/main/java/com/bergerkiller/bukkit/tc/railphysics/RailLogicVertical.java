package com.bergerkiller.bukkit.tc.railphysics;

import org.bukkit.block.BlockFace;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;

public class RailLogicVertical extends RailLogic {
	private static final RailLogicVertical[] values = new RailLogicVertical[4];
	static {
		for (int i = 0; i < 4; i++) {
			values[i] = new RailLogicVertical(FaceUtil.notchToFace(i << 1));
		}
	}

	private RailLogicVertical(BlockFace direction) {
		super(direction);
	}

	@Override
	public double getForwardVelocity(MinecartMember<?> member) {
		return member.getDirection().getModY() * member.getEntity().vel.getY();
	}

	@Override
	public void setForwardVelocity(MinecartMember<?> member, double force) {
		member.getEntity().vel.setY(member.getDirection().getModY() * force);
	}

	@Override
	public boolean hasVerticalMovement() {
		return true;
	}

	@Override
	public double getGravityMultiplier(MinecartMember<?> member) {
		return member.getGroup().getProperties().isSlowingDown() ? MinecartMember.VERTRAIL_MULTIPLIER : 0.0;
	}

	@Override
	public void onPreMove(MinecartMember<?> member) {
		final CommonMinecart<?> entity = member.getEntity();
		// Horizontal rail force to motY
		entity.vel.y.add(entity.vel.xz.length() * member.getDirection().getModY());
		entity.vel.xz.setZero();
		// Position update
		entity.loc.x.set(member.getBlockPos().midX());
		entity.loc.z.set(member.getBlockPos().midZ());
	}

	/**
	 * Gets the vertical rail logic for the direction specified
	 * 
	 * @param direction of the rail
	 * @param upSlope whether the minecart went up from a slope
	 * @return Rail Logic
	 */
	public static RailLogicVertical get(BlockFace direction) {
		return values[FaceUtil.faceToNotch(direction) >> 1];
	}
}
