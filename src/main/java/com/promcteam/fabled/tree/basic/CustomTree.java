package com.promcteam.fabled.tree.basic;

import com.promcteam.fabled.Fabled;
import com.promcteam.fabled.api.classes.FabledClass;
import com.promcteam.fabled.api.exception.SkillTreeException;
import com.promcteam.fabled.api.skills.Skill;
import com.promcteam.fabled.gui.tool.GUIData;
import com.promcteam.fabled.gui.tool.GUIType;
import com.promcteam.codex.mccore.config.parse.DataSection;

import java.util.List;

public class CustomTree extends InventoryTree {
    /**
     * Constructor
     *
     * @param api  api reference
     * @param tree
     */
    public CustomTree(Fabled api, FabledClass tree) {
        super(api, tree);
    }

    @Override
    protected void arrange(List<Skill> skills) throws SkillTreeException {
        skillSlots.clear();
        height = 3;
        DataSection section =
                Fabled.getConfig("gui").getConfig().getSection(GUIType.SKILL_TREE.getPrefix() + tree.getName());
        if (section == null) {
            return;
        }

        height = Math.max(1, Math.min(section.getInt(GUIData.ROWS, 3), 6));
        DataSection slotsSection = section.getSection(GUIData.SLOTS);
        if (slotsSection == null) {
            return;
        }
        for (String key : slotsSection.keys()) {
            int         page        = Integer.parseInt(key);
            DataSection pageSection = slotsSection.getSection(key);
            for (String skillName : pageSection.keys()) {
                Skill skill = Fabled.getSkill(skillName);
                if (skill == null) {
                    continue;
                }
                skillSlots.put(pageSection.getInt(skillName) + (page - 1) * height * 9, skill);
            }
        }
    }
}