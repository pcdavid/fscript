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
package org.objectweb.fractal.fscript;

import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.Level;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.util.monolog.api.LoggerFactory;

/**
 * Simple logger component wrapping a configured Monolog logger.
 * 
 * @author Pierre-Charles David
 */
public class LoggerImpl implements Logger {
    private static final LoggerFactory FACTORY = Monolog
            .getMonologFactory("fscript-logging.properties");

    private final Logger logger;

    public LoggerImpl() {
        logger = FACTORY.getLogger("org.objectweb.fractal.fscript");
    }

    public Object getAttribute(String attr) {
        return logger.getAttribute(attr);
    }

    public String[] getAttributeNames() {
        return logger.getAttributeNames();
    }

    public int getCurrentIntLevel() {
        return logger.getCurrentIntLevel();
    }

    public Level getCurrentLevel() {
        return logger.getCurrentLevel();
    }

    public String getName() {
        return logger.getName();
    }

    public String getType() {
        return logger.getType();
    }

    public boolean isLoggable(int level) {
        return logger.isLoggable(level);
    }

    public boolean isLoggable(Level arg0) {
        return logger.isLoggable(arg0);
    }

    public boolean isOn() {
        return logger.isOn();
    }

    public void log(int level, Object arg1, Object arg2, Object arg3) {
        logger.log(level, arg1, arg2, arg3);
    }

    public void log(int level, Object arg1, Throwable arg2, Object arg3, Object arg4) {
        logger.log(level, arg1, arg2, arg3, arg4);
    }

    public void log(int level, Object arg1, Throwable arg2) {
        logger.log(level, arg1, arg2);
    }

    public void log(int level, Object arg1) {
        logger.log(level, arg1);
    }

    public void log(Level level, Object arg1, Object arg2, Object arg3) {
        logger.log(level, arg1, arg2, arg3);
    }

    public void log(Level level, Object arg1, Throwable arg2, Object arg3, Object arg4) {
        logger.log(level, arg1, arg2, arg3, arg4);
    }

    public void log(Level level, Object arg1, Throwable arg2) {
        logger.log(level, arg1, arg2);
    }

    public void log(Level level, Object obj) {
        logger.log(level, obj);
    }

    public Object setAttribute(String attr, Object value) {
        return logger.setAttribute(attr, value);
    }

    public void setIntLevel(int level) {
        logger.setIntLevel(level);
    }

    public void setLevel(Level level) {
        logger.setLevel(level);
    }

    public void setName(String name) {
        logger.setName(name);
    }

    public void turnOff() {
        logger.turnOff();
    }

    public void turnOn() {
        logger.turnOn();
    }
}
