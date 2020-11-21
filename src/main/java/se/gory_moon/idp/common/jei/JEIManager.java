package se.gory_moon.idp.common.jei;

import se.gory_moon.idp.common.base.BaseManager;

public class JEIManager extends BaseManager<JEIData> {
    public JEIManager() {
        super("jei_info", JEIData::new);
    }

    @Override
    protected String getDataName() {
        return "infos";
    }
}
