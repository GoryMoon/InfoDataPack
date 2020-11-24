package se.gory_moon.idp.common.tooltip;

import se.gory_moon.idp.common.base.BaseData;
import se.gory_moon.idp.common.base.BaseManager;

public class TooltipManager extends BaseManager {
    public TooltipManager() {
        super("tooltips", BaseData::new);
    }

    @Override
    protected String getDataName() {
        return "tooltips";
    }
}
