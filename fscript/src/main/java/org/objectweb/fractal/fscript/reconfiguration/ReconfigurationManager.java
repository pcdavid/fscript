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

import java.util.HashMap;
import java.util.Map;

public class ReconfigurationManager {
    private static final ReconfigurationManager INSTANCE = new ReconfigurationManager();

    public static ReconfigurationManager getInstance() {
        return INSTANCE;
    }

    private Map<Thread, Transaction> currentTransactions;

    private ReconfigurationManager() {
        currentTransactions = new HashMap<Thread, Transaction>();
    }

    public boolean asTransactionDo(Runnable work) {
        if (getCurrentTransaction() != null) {
            throw new IllegalStateException(
                    "A transaction is already started for thread "
                            + Thread.currentThread() + ".");
        }
        try {
            begin();
            work.run();
            commit();
            return true;
        } catch (Exception th) {
            cancel();
            return false;
        }
    }

    public void begin() throws IllegalStateException {
        if (getCurrentTransaction() != null) {
            throw new IllegalStateException(
                    "A transaction is already started for thread "
                            + Thread.currentThread() + ".");
        } else {
            Transaction tx = new Transaction();
            synchronized (currentTransactions) {
                currentTransactions.put(Thread.currentThread(), tx);
            }
            tx.start();
        }
    }

    public void commit() {
        Thread th = Thread.currentThread();
        Transaction tx = currentTransactions.get(th);
        if (tx != null) {
            tx.commit();
            currentTransactions.remove(th);
        } else {
            throw new IllegalStateException("No current transaction for thread " + th
                    + ".");
        }
    }

    public void cancel() {
        Thread th = Thread.currentThread();
        Transaction tx = currentTransactions.get(th);
        if (tx != null) {
            tx.cancel();
            currentTransactions.remove(th);
        } else {
            throw new IllegalStateException("No current transaction for thread " + th
                    + ".");
        }
    }

    public Transaction getCurrentTransaction() {
        return currentTransactions.get(Thread.currentThread());
    }
}
