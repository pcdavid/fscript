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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jline.Completor;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class PrefixCompletor implements Completor {
    private final char delimiter;

    public PrefixCompletor(char delimiter) {
        this.delimiter = delimiter;
    }

    @SuppressWarnings("unchecked")
    public int complete(String buffer, int position, List candidates) {
        int start = buffer.lastIndexOf(delimiter, position);
        if (start != -1) {
            String prefix = buffer.substring(start + 1, position);
            candidates.addAll(getCandidatesStartingWith(prefix));
            Collections.sort(candidates);
        }
        return start + 1;
    }

    private Collection<String> getCandidatesStartingWith(final String prefix) {
        Set<String> values = getAllPossibleValues();
        List<String> candidates = Lists.newArrayList();
        Iterables.addAll(candidates, Iterables.filter(values, new Predicate<String>() {
            public boolean apply(String name) {
                return name.startsWith(prefix);
            }
        }));
        return candidates;
    }

    protected abstract Set<String> getAllPossibleValues();
}
