package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
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
    public void sampleOccurred(@NonNull ISample result) {
        sample = result;
    }
}
