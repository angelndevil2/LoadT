package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.ResultType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author k, Created on 16. 2. 15.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsoleResultViewer extends ResultViewer {

    private static final long serialVersionUID = 4134551329224436365L;

    public ResultType getResultType() {
        return ResultType.EACH;
    }

    @Override
    public void sampleOccurred(@NonNull ISample result) {
        super.sampleOccurred(result);
        view();
    }

    public void view() {
        System.out.println(getSample().toPrintableString());
    }
}
