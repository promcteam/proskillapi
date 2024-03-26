package com.promcteam.fabled.dynamic.trigger;

import com.promcteam.fabled.api.CastData;
import com.promcteam.fabled.api.Settings;
import com.promcteam.fabled.api.event.PhysicalDamageEvent;
import org.bukkit.entity.LivingEntity;

/**
 * Fabled © 2024
 * com.promcteam.fabled.dynamic.trigger.BlockBreakTrigger
 */
public class PhysicalDealtTrigger extends PhysicalTrigger {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return "PHYSICAL_DAMAGE";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LivingEntity getCaster(final PhysicalDamageEvent event) {
        return event.getDamager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LivingEntity getTarget(final PhysicalDamageEvent event, final Settings settings) {
        return isUsingTarget(settings) ? event.getTarget() : event.getDamager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(final PhysicalDamageEvent event, final CastData data) {
        data.put("api-dealt", event.getDamage());
    }
}
