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
package org.objectweb.fractal.fscript.reconfiguration;

public abstract class AbstractReconfiguration implements Reconfiguration {
    public Object apply() throws ReconfigurationFailedException {
        try {
            Transaction tx = ReconfigurationManager.getInstance().getCurrentTransaction();
            if (tx == null) {
                throw new IllegalStateException(
                        "Reconfiguration called outside of a transaction.");
            }
            return apply(tx);
        } catch (Throwable th) {
            throw new ReconfigurationFailedException(th);
        }
    }

    protected abstract Object apply(Transaction tx) throws Throwable;
}
