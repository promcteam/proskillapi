package com.sucy.skill.dataload;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.testutil.MockedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DataLoadTest extends MockedTest {

    @Override
    public void preInit() {
        loadClasses("Honor Guard");
        loadSkills("Brilliance Strike");
    }

    @Test
    void honorGuardLoads() {
        assertNotNull(SkillAPI.getClass("Honor Guard"));
    }

    @Test
    void brillianceStrikeLoads() {
        assertNotNull(SkillAPI.getSkill("Brilliance Strike"));
    }

    @Test
    void masterFilesLoad() {
        unloadClasses("Honor Guard");
        unloadSkills("Brilliance Strike");
        useClasses("full/classes.yml");
        useSkills("full/skills.yml");
        reload();

        assertEquals(8, SkillAPI.getClasses().size());
        assertEquals(41, SkillAPI.getSkills().size());

        SkillAPI.getClasses().forEach((name, clazz) -> {
            assert (!clazz.getSkills().isEmpty());
        });
    }
}