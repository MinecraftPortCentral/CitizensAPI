package net.citizensnpcs.api.ai.goals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.citizensnpcs.api.ai.event.CancelReason;
import net.citizensnpcs.api.ai.event.NavigatorCallback;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;

public class TargetNearbyEntityGoal extends BehaviorGoalAdapter {
    private final boolean aggressive;
    private boolean finished;
    private final NPC npc;
    private final double radius;
    private CancelReason reason;
    private Entity target;
    private final List<EntityType> targets;

    private TargetNearbyEntityGoal(NPC npc, List<EntityType> targets, boolean aggressive, double radius) {
        this.npc = npc;
        this.targets = targets;
        this.aggressive = aggressive;
        this.radius = radius;
    }

    @Override
    public void reset() {
        npc.getNavigator().cancelNavigation();
        target = null;
        finished = false;
        reason = null;
    }

    @Override
    public BehaviorStatus run() {
        if (finished) {
            return reason == null ? BehaviorStatus.SUCCESS : BehaviorStatus.FAILURE;
        }
        return BehaviorStatus.RUNNING;
    }

    @Override
    public boolean shouldExecute() {
        if (targets.size() == 0 || !npc.isSpawned())
            return false;
        Collection<Entity> nearby = npc.getEntity().getNearbyEntities(radius);
        this.target = null;
        for (Entity entity : nearby) {
            if (targets.contains(entity.getType())) {
                target = entity;
                break;
            }
        }
        if (target != null) {
            npc.getNavigator().setTarget(target, aggressive);
            npc.getNavigator().getLocalParameters().addSingleUseCallback(new NavigatorCallback() {
                @Override
                public void onCompletion(CancelReason cancelReason) {
                    reason = cancelReason;
                    finished = true;
                }
            });
            return true;
        }
        return false;
    }

    public static class Builder {
        private boolean aggressive;
        private final NPC npc;
        private double radius = 10D;
        private List<EntityType> targetTypes = new ArrayList<>();

        public Builder(NPC npc) {
            this.npc = npc;
        }

        public Builder aggressive(boolean aggressive) {
            this.aggressive = aggressive;
            return this;
        }

        public TargetNearbyEntityGoal build() {
            return new TargetNearbyEntityGoal(npc, targetTypes, aggressive, radius);
        }

        public Builder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder targets(List<EntityType> targetTypes) {
            this.targetTypes = targetTypes;
            return this;
        }
    }

    public static Builder builder(NPC npc) {
        return new Builder(npc);
    }
}
