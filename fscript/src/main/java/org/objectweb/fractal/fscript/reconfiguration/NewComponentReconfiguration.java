package org.objectweb.fractal.fscript.reconfiguration;

import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;

public class NewComponentReconfiguration extends AbstractReconfiguration {
    private final GenericFactory genericFactory;

    private final ComponentType type;

    private final String membraneDesc;

    private final String contentDesc;

    public NewComponentReconfiguration(GenericFactory gf, ComponentType type,
            String membraneDesc, String contentDesc) {
        this.genericFactory = gf;
        this.type = type;
        this.membraneDesc = membraneDesc;
        if ("".equals(contentDesc)) {
            this.contentDesc = null;
        } else {
            this.contentDesc = contentDesc;
        }
    }

    @Override
    protected Object apply(Transaction tx) throws InstantiationException {
        return genericFactory.newFcInstance(type, membraneDesc, contentDesc);
    }
}
