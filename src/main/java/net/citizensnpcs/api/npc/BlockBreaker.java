package net.citizensnpcs.api.npc;

import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import org.spongepowered.api.item.inventory.ItemStack;

public abstract class BlockBreaker extends BehaviorGoalAdapter {
    public static class BlockBreakerConfiguration {
        private Runnable callback;
        private ItemStack itemStack;
        private float modifier = 1;
        private double radius = 0;

        public float blockStrengthModifier() {
            return modifier;
        }

        public BlockBreakerConfiguration blockStrengthModifier(float modifier) {
            this.modifier = modifier;
            return this;
        }

        public Runnable callback() {
            return callback;
        }

        public BlockBreakerConfiguration callback(Runnable callback) {
            this.callback = callback;
            return this;
        }

        public ItemStack item() {
            return itemStack;
        }

        public BlockBreakerConfiguration item(ItemStack stack) {
            itemStack = stack;
            return this;
        }

        public BlockBreakerConfiguration radius(double radius) {
            this.radius = radius;
            return this;
        }

        public double radiusSquared() {
            return Math.pow(radius, 2);
        }
    }

    public static final BlockBreakerConfiguration EMPTY_CONFIG = new BlockBreakerConfiguration();
}
