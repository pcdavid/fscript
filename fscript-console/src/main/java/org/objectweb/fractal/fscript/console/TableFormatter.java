/*
 * Copyright (c) 2007-2008 ARMINES
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.console;

import com.google.common.base.Preconditions;
import com.google.common.collect.Comparators;

/**
 * Format tabular data properly on an <code>TextConsole</code>.
 * 
 * @author Pierre-Charles David
 */
public class TableFormatter {
    /**
     * How much space to put between columns of tabular results.
     */
    private static final int TABLE_PADDING = 4;

    private final TextConsole console;

    private final String[][] table;

    public TableFormatter(TextConsole console, String[][] table) {
        this.console = console;
        this.table = table;
        Preconditions.checkNotNull(table);
        Preconditions.checkArgument(table.length > 0, "No table header.");
    }
    
    public void print() {
        int[] columnsWidths = computeWidths(table, TABLE_PADDING);
        String[] headers = table[0];
        showTableRow(headers, columnsWidths);
        showSeparatorRow(columnsWidths);
        for (int i = 1; i < table.length; i++) {
            showTableRow(table[i], columnsWidths);
        }
    }
    
    private void showTableRow(String[] row, int[] columnsWidths) {
        assert row.length == columnsWidths.length;
        int i = 0;
        for (String cell : row) {
            String value = (cell == null) ? "null" : cell;
            console.printString(value);
            console.fill(' ', columnsWidths[i++] - value.length());
        }
        console.newline();
    }

    private void showSeparatorRow(int[] columnsWidths) {
        for (int width : columnsWidths) {
            console.fill('-', width);
        }
        console.newline();
    }

    private int[] computeWidths(String[][] table, int padding) {
        int[] widths = new int[table[0].length];
        // Compute the minimum width to hold the data
        for (String[] row : table) {
            int column = 0;
            for (String cell : row) {
                int width = (cell != null) ? cell.length() : "null".length();
                widths[column] = Comparators.max(widths[column], width);
                column++;
            }
        }
        // Add padding to all but the last column
        for (int i = 0; i < widths.length - 1; i++) {
            widths[i] += padding;
        }
        return widths;
    }
}
