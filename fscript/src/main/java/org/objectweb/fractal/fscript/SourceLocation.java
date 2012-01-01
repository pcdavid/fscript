/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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
package org.objectweb.fractal.fscript;

/**
 * Represents a segment of a source document
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class SourceLocation {
    private final String sourceName;

    private final int startLine;

    private final int startColumn;

    private final int endLine;

    private final int endColumn;

    public SourceLocation(final String sourceName, final int startLine,
            final int startColumn, final int endLine, final int endColumn) {
        if (startLine < 0 || startColumn < 0 || endLine < 0 || endColumn < 0) {
            throw new IllegalArgumentException(
                    "Invalid source coordinates: can not be negative.");
        }
        if (startLine > endLine || (startLine == endLine && startColumn > endColumn)) {
            throw new IllegalArgumentException(
                    "Invalid source extent: can not stop before it starts.");
        }
        if (sourceName != null) {
            this.sourceName = sourceName;
        } else {
            this.sourceName = "<string>";
        }
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public SourceLocation(final String source, final int startLine, final int startColumn) {
        this(source, startLine, startColumn, startLine, startColumn);
    }

    public String getSourceName() {
        return sourceName;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    /**
     * Format:
     * <ul>
     * <li><code>source:line:column</code> if start and end points coincide.</li>
     * <li><code>source:line:column,line:column</code> otherwise.</li>
     * </ul>
     */
    @Override
    public String toString() {
        String str = sourceName + ":" + startLine + ":" + startColumn;
        if (startLine != endLine || startColumn != endColumn) {
            str = str + "," + endLine + ":" + endColumn;
        }
        return str;
    }
}
