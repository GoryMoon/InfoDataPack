package se.gory_moon.idp.common.jei;

import se.gory_moon.idp.common.base.BaseData;
import se.gory_moon.idp.common.base.BaseManager;

public class JEIManager extends BaseManager {
    public JEIManager() {
        super("jei_info", BaseData::new);
    }

    @Override
    protected String getDataName() {
        return "infos";
    }
}
