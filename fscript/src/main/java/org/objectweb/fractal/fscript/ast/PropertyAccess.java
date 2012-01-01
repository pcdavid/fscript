package org.objectweb.fractal.fscript.ast;

import com.google.common.base.Preconditions;

public abstract class PropertyAccess extends ASTNode {
    private final ASTNode nodeExpression;
    private final String propertyName;

    public PropertyAccess(SourceLocation loc, ASTNode nodeExpression, String propName) {
        super(loc);
        Preconditions.checkNotNull(nodeExpression, "Missing node expression.");
        Preconditions.checkNotNull(propName, "Missing property name.");
        this.nodeExpression = nodeExpression;
        this.propertyName = propName;
    }
    
    public ASTNode getNodeExpression() {
        return nodeExpression;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
}
