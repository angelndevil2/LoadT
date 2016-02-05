package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.loadmanager.ILoadManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Set options and prepare other stuff for {@link ILoadManager}
 *
 * @author k, Created on 16. 2. 5.
 */
@Data
@Slf4j
public class LoadT {

    private ILoadManager loadManager;

}
