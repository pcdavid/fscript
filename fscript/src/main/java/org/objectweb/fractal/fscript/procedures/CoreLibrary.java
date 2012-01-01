package org.objectweb.fractal.fscript.procedures;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * This library provides all the core, pre-defined procedures defined in the language
 * itself. All these procedures are independent of any specific target model.
 * 
 * @author Pierre-Charles David
 */
public class CoreLibrary implements NativeLibrary {
    private final Map<String, NativeProcedure> procedures = Maps.newHashMap();

    public CoreLibrary() {
        registerCoreProcedures();
    }

    private void registerCoreProcedures() {
        register(new AdditionFunction());
        register(new ConcatFunction());
        register(new DifferentFunction());
        register(new DivisionFunction());
        register(new EndsWithFunction());
        register(new EqualsFunction());
        register(new FalseFunction());
        register(new GreaterThanFunction());
        register(new GreaterThanOrEqualFunction());
        register(new LessThanOrEqualFunction());
        register(new LessThanFunction());
        register(new MatchesFunction());
        register(new MinusFunction());
        register(new MultiplicationFunction());
        register(new NotFunction());
        register(new SizeFunction());
        register(new StartsWithFunction());
        register(new SubstractionFunction());
        register(new TrueFunction());
        register(new CurrentFunction());
        register(new UnionFunction());
        register(new IntersectionFunction());
        register(new DifferenceFunction());
        register(new EchoAction());
        register(new FailAction());
    }

    private void register(NativeProcedure proc) {
        procedures.put(proc.getName(), proc);
    }

    public Set<String> getAvailableProcedures() {
        return Collections.unmodifiableSet(new HashSet<String>(procedures.keySet()));
    }

    public NativeProcedure getNativeProcedure(String name) {
        return procedures.get(name);
    }

    public boolean hasProcedure(String name) {
        return procedures.containsKey(name);
    }

    @Override
    public String toString() {
        return "Core library";
    }
}
