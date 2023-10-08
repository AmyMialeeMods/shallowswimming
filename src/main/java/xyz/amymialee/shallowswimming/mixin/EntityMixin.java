package xyz.amymialee.shallowswimming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow private BlockPos blockPos;
    @Shadow public World world;

    @WrapOperation(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSubmergedInWater()Z"))
    private boolean shallowSwimming$notSubmerged(@NotNull Entity instance, Operation<Boolean> original) {
        System.out.println("isSubmergedInWater: " + original.call(instance));
        return instance.isTouchingWater();
    }

    @WrapOperation(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"))
    private boolean shallowSwimming$blockPosition(FluidState instance, TagKey<Fluid> tag, @NotNull Operation<Boolean> original) {
        System.out.println("isIn: " + original.call(instance, tag));
        return original.call(instance, tag) || original.call(this.world.getFluidState(this.blockPos.down()), tag);
    }
}