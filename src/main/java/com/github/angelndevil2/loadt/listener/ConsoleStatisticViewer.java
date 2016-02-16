package com.github.angelndevil2.loadt.listener;

/**
 * @author k, Created on 16. 2. 15.
 */
public class ConsoleStatisticViewer extends StatisticViewer {

    private static final long serialVersionUID = 7911055367231610516L;

    public void view() {
        System.out.println(getStatistics().toPrintableString());
    }
}
