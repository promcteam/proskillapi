/**
 * Fabled
 * com.promcteam.fabled.dynamic.mechanic.value.ValueSetMechanic
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2024 ProMCTeam
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.promcteam.fabled.dynamic.mechanic.value;

import com.promcteam.fabled.Fabled;
import com.promcteam.fabled.api.CastData;
import com.promcteam.fabled.api.player.PlayerData;
import com.promcteam.fabled.dynamic.DynamicSkill;
import com.promcteam.fabled.dynamic.mechanic.MechanicComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * Load data from persistent data into cast data
 */
public class ValueLoadMechanic extends MechanicComponent {
    private static final String KEY      = "key";
    private static final String OVERRIDE = "override";
    private static final String SAVE     = "save";

    @Override
    public String getKey() {
        return "value load";
    }

    /**
     * Executes the component
     *
     * @param caster  caster of the skill
     * @param level   level of the skill
     * @param targets targets to apply to
     * @param force
     * @return true if applied to something, false otherwise
     */
    @Override
    public boolean execute(LivingEntity caster, int level, List<LivingEntity> targets, boolean force) {
        String     key        = settings.getString(KEY);
        boolean    override   = settings.getBool(OVERRIDE);
        CastData   data       = DynamicSkill.getCastData(caster);
        PlayerData playerData = Fabled.getPlayerData((OfflinePlayer) caster);
        if (!playerData.getAllPersistentData().containsKey(key)) return false;
        if (!data.contains(key) || override) {
            data.put(key, playerData.getPersistentData(key));
        }
        if (!override && settings.getBool(SAVE, false))
            Fabled.getPlayerData((OfflinePlayer) caster).setPersistentData(key, data.getRaw(key));
        return true;
    }
}