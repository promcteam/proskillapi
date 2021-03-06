/**
 * SkillAPI
 * com.sucy.skill.task.CooldownTask
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Steven Sucy
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
package com.sucy.skill.task;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.thread.RepeatThreadTask;
import mc.promcteam.engine.mccore.util.VersionManager;
import org.bukkit.entity.Player;

/**
 * Handles updating cooldown values on skill bars for players
 */
public class CooldownTask extends RepeatThreadTask {
    /**
     * Initializes a new cooldown task. This shouldn't be used by
     * other plugins as it is already set up by the API and additional
     * copies would create extra processing for no real gain.
     */
    public CooldownTask() {
        super(20, 20);
    }

    /**
     * Updates the cooldowns on skill bars each second
     */
    @Override
    public void run() {
        for (Player player : VersionManager.getOnlinePlayers()) {
            if (!SkillAPI.hasPlayerData(player)) continue;

            PlayerData data = SkillAPI.getPlayerData(player);
            if (data.hasClass()) data.getSkillBar().updateCooldowns();
        }
    }
}
