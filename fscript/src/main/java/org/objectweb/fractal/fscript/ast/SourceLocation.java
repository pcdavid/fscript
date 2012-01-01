/*
 * Copyright (c) 2006-2008 ARMINES
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
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.ast;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;

import org.objectweb.fractal.fscript.diagnostics.Diagnostic;

import net.jcip.annotations.Immutable;

/**
 * Represents a segment of a source document. Lines and columns are indexed from one.
 * 
 * @see Diagnostic
 * @author Pierre-Charles David
 */
@Immutable
public class SourceLocation {
    /**
     * A constant to use instead of <code>null</code> to indicate, for example in a diagnostic,
     * that the source location is not known.
     */
    public static final SourceLocation UNKNOWN = new SourceLocation("<unknown>", 1, 1);

    /**
     * A constant to use instead of <code>null</code> to indicate that a diagnostic is not related
     * to a specific source location.
     */
    public static final SourceLocation NONE = new SourceLocation("<none>", 1, 1);

    private final Object sourceDocument;

    private final int startLine;

    private final int startColumn;

    private final int endLine;

    private final int endColumn;

    /**
     * Creates a new <code>SourceLocation</code>.
     * 
     * @param sourceDocument
     *            the source document. May be a {@link File} or a plain <code>String</code>.
     * @param startLine
     *            the line number where this location starts in the source document. Lines are
     *            numbered starting from one.
     * @param startColumn
     *            the column number where this location starts in the source document. Columns are
     *            numbered starting from one.
     * @param endLine
     *            the line number where this location ends in the source document.
     * @param endColumn
     *            the column number where this location ends in the source document.
     */
    public SourceLocation(final Object sourceDocument, final int startLine, final int startColumn,
            final int endLine, final int endColumn) {
        checkArgument(startLine > 0 && startColumn > 0 && endLine > 0 && endColumn > 0,
                "Invalid source coordinates: must be positive.");
        checkArgument(startLine < endLine || (startLine == endLine && startColumn <= endColumn),
                "Invalid source extent: can not stop before it starts.");
        this.sourceDocument = sourceDocument;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    /**
     * Creates a new <code>SourceLocation</code> which references a single character in the source
     * document.
     * 
     * @param sourceDocument
     *            the source document. May be a {@link File} or a plain <code>String</code>.
     * @param startLine
     *            the line number where this location starts in the source document. Lines are
     *            numbered starting from one.
     * @param startColumn
     *            the column number where this location starts in the source document. Columns are
     *            numbered starting from one.
     */
    public SourceLocation(final Object source, final int startLine, final int startColumn) {
        this(source, startLine, startColumn, startLine, startColumn);
    }

    public Object getSourceDocument() {
        return sourceDocument;
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
     * <li><code>source: line:column</code> if start and end points coincide.</li>
     * <li><code>source: line:column,line:column</code> otherwise.</li>
     * </ul>
     */
    @Override
    public String toString() {
        if (this == UNKNOWN) {
            return "<unknown>";
        } else if (this == NONE) {
            return "<none>";
        } else {
            String sourceName = null;
            if (sourceDocument != null) {
                sourceName = sourceDocument.toString();
            } else {
                sourceName = "<string>";
            }
            String str = sourceName + ": [" + startLine + ":" + startColumn;
            if (startLine != endLine || startColumn != endColumn) {
                str = str + ", " + endLine + ":" + endColumn;
            }
            return str + "]";
        }
    }
}
