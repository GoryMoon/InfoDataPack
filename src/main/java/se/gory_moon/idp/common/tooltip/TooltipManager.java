package se.gory_moon.idp.common.tooltip;

import se.gory_moon.idp.common.base.BaseManager;

public class TooltipManager extends BaseManager<TooltipData> {
    public TooltipManager() {
        super("tooltips", TooltipData::new);
    }

    @Override
    protected String getDataName() {
        return "tooltips";
    }
}
