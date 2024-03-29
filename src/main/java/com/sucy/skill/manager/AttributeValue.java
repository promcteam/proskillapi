package com.sucy.skill.manager;

import com.sucy.skill.data.formula.Formula;
import com.sucy.skill.data.formula.value.CustomValue;
import com.sucy.skill.dynamic.EffectComponent;
import com.sucy.skill.log.LogType;
import com.sucy.skill.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
     * Represents one formula modifier for an attribute
     * that can have conditions
     */
    public class AttributeValue {
        private Formula             formula;
        private Map<String, String> conditions = new HashMap<>();

        /**
         * Loads the attribute value that starts with the formula
         * and can have as many conditions as desired after
         *
         * @param data data string for the value
         */
        public AttributeValue(String data) {
            String[] pieces = data.split(":");
            formula = new Formula(pieces[0], new CustomValue("v"), new CustomValue("a"));
            for (int i = 1; i < pieces.length; i++) {
                String[] sides = pieces[i].split("=");
                conditions.put(sides[0], sides[1]);
                Logger.log(LogType.ATTRIBUTE_LOAD, 3, "      Condition: " + sides[0] + " / " + sides[1]);
            }
        }

        /**
         * Checks whether the formula should be applied to the component
         *
         * @param component component to check for conditions against
         * @return true if passes the conditions
         */
        public boolean passes(EffectComponent component) {
            for (String key : conditions.keySet()) {
                if (!component.getSettings().getString(key).equalsIgnoreCase(conditions.get(key))) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks the conditions for the given component
         *
         * @param value  base value
         * @param amount amount of attribute points
         * @return the modified value if the conditions passed or the base value if they failed
         */
        public double apply(double value, int amount) {
            return formula.compute(value, amount);
        }
    }