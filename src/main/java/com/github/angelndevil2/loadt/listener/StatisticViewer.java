package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.ResultType;
import com.github.angelndevil2.loadt.common.StatisticSample;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class StatisticViewer extends ResultViewer {

    private static final long serialVersionUID = 3643254266110216616L;

    private StatisticSample statistics;

    @Override
    public ResultType getResultType() {
        return ResultType.STATISTIC;
    }

    @Override
    public void sampleOccurred(@NonNull ISample sample) {
        statistics = (StatisticSample) sample;
        view();
    }
}
