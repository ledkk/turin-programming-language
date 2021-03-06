package me.tomassetti.turin.parser.ast.expressions;

import com.google.common.collect.ImmutableList;
import me.tomassetti.turin.parser.ast.Node;
import me.tomassetti.turin.parser.ast.QualifiedName;

public class TypeIdentifier extends Node {
    private QualifiedName packageName;
    private String typeName;

    public TypeIdentifier(QualifiedName packageName, String typeName) {
        this.packageName = packageName;
        this.typeName = typeName;
    }

    @Override
    public Iterable<Node> getChildren() {
        return ImmutableList.of(packageName);
    }

    public String qualifiedName() {
        if (packageName == null) {
            return typeName;
        } else {
            return packageName.qualifiedName() + "." + typeName;
        }
    }
}
