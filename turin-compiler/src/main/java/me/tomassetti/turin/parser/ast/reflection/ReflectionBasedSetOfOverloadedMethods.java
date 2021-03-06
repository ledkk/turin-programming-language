package me.tomassetti.turin.parser.ast.reflection;

import me.tomassetti.turin.jvm.JvmMethodDefinition;
import me.tomassetti.turin.jvm.JvmType;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.Node;
import me.tomassetti.turin.parser.ast.expressions.Expression;
import me.tomassetti.turin.parser.ast.typeusage.TypeUsage;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

public class ReflectionBasedSetOfOverloadedMethods extends Expression {

    private List<Method> methods;
    private boolean isStatic;
    private Node instance;
    private String name;

    @Override
    public JvmMethodDefinition findMethodFor(List<JvmType> argsTypes, Resolver resolver, boolean staticContext) {
        return ReflectionBasedMethodResolution.findMethodAmong(name, argsTypes, resolver, staticContext, methods, this);
    }

    public ReflectionBasedSetOfOverloadedMethods(List<Method> methods, Node instance) {
        if (methods.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.isStatic = Modifier.isStatic(methods.get(0).getModifiers());
        this.name = methods.get(0).getName();
        for (Method method : methods) {
            if (isStatic != Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException("All methods should be static or non static");
            }
            if (!name.equals(method.getName())) {
                throw new IllegalArgumentException("All methods should be named " + name);
            }
        }
        this.methods = methods;
        this.instance = instance;
    }

    public Node getInstance() {
        return instance;
    }

    @Override
    public Iterable<Node> getChildren() {
        return Collections.emptyList();
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public TypeUsage calcType(Resolver resolver) {
        throw new UnsupportedOperationException();
    }
}
