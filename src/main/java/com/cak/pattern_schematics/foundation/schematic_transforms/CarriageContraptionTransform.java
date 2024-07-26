package com.cak.pattern_schematics.foundation.schematic_transforms;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

public class CarriageContraptionTransform extends ContraptionSchematicTransform<CarriageContraption> {
    
    public CarriageContraptionTransform() {
        super(CarriageContraption.class);
    }
    
    @Override
    public BlockPos modifyPos(CarriageContraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos globalPos) {
//        Vec3 rotationAnchor = getRotationAnchor(patternSchematicWorld);
//        System.out.println("a");
//        return toVec3i(
//            Vec3.atCenterOf(globalPos.subtract(getMinBounds(patternSchematicWorld)))
//                .subtract(rotationAnchor)
//                .yRot((float) (getRotationOfTrainContraption(currentContraption).ordinal() * (Math.PI/2)))
//                .add(rotationAnchor)
//        ).offset(getMinBounds(patternSchematicWorld));
        
        //currentContraption.entity.getAnchorVec()
        
        
        return globalPos.rotate(getRotationOfTrainContraption(currentContraption));
    }
    private BlockPos toVec3i(Vec3 vec3) {
        return new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z).offset(1,1,1);
    }
    
    private Vec3i getMinBounds(PatternSchematicWorld patternSchematicWorld) {
        BoundingBox bounds = patternSchematicWorld.getBounds();
        return new Vec3i(bounds.minX(), bounds.minY(), bounds.minZ());
    }
    
    private Vec3 getRotationAnchor(PatternSchematicWorld patternSchematicWorld) {
        Vec3i lengths = patternSchematicWorld.getBounds().getLength();
        return Vec3.atLowerCornerOf(lengths).scale(0.5);
    }
    
    //    @Override
//    public Vec3i getSourceLengths(CarriageContraption currentContraption, PatternSchematicWorld patternSchematicWorld) {
//        return Vec3iUtils.abs(new BlockPos(super.getSourceLengths(currentContraption, patternSchematicWorld))
//            .rotate(getRotationOfTrainContraption(currentContraption)));
//    }
    @Override
    public BlockState modifyState(CarriageContraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockState blockState, BlockPos position, LevelAccessor level) {
        return super.modifyState(currentContraption, patternSchematicWorld, blockState, position, level)
            .rotate(level, position, getRotationOfTrainContraption(currentContraption));
    }
    
    public boolean inRange(double a, double b, double range) {
        double dist = range / 2f;
        return b >= a - dist && b <= a + dist;
    }
    
    public Rotation getRotationOfAngle(double radians) {
        for (Rotation rotation : Rotation.values())
            if (inRange(rotation.ordinal() * (Math.PI / 2), radians, Math.PI / 2))
                return rotation;
        System.out.println("a");
        return Rotation.NONE;
    }
    
    public Rotation getRotationOfTrainContraption(CarriageContraption contraption) {
        Vec3 orientation = contraption.entity.applyRotation(new Vec3(1, 0, 0), 1f);
        
        Rotation rotation = getRotationOfAngle(Math.atan2(orientation.x, orientation.z));
        
        return rotation;
    }
    
    protected static Rotation getRotation(Direction direction) {
        return Rotation.values()[direction.get2DDataValue()];
    }
    
}
