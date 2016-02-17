package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
public abstract class ResultViewer implements IResultViewer {

    private static final long serialVersionUID = 1046943735795547759L;
    @Setter(AccessLevel.NONE)
    private ISample sample;
    private long lastViewTime;

    public void sampleOccurred(@NonNull ISample result) {
        sample = result;
    }

    /**
     * @return view interval in millis
     */
    @Override
    public long getViewInterval() {
        return ContextUtil.getViewInterval();
    }

    /**
     * check {@link com.github.angelndevil2.loadt.common.ViewOptions} and view or not
     *
     */
    @Override
    public void viewOrNot(@NonNull final ISample sample) {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastViewTime > getViewInterval()) {
            view();
            lastViewTime = currentTime;
        }
    }
}
