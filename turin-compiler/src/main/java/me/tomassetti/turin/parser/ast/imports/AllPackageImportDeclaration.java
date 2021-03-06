package me.tomassetti.turin.parser.ast.imports;

import com.google.common.collect.ImmutableList;
import me.tomassetti.turin.jvm.JvmNameUtils;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.Node;
import me.tomassetti.turin.parser.ast.QualifiedName;
import me.tomassetti.turin.parser.ast.TypeDefinition;

import java.util.Optional;

public class AllPackageImportDeclaration extends ImportDeclaration {

    private QualifiedName qualifiedName;

    public AllPackageImportDeclaration(QualifiedName qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public Optional<Node> findAmongImported(String name, Resolver resolver) {
        // TODO correct the context passed
        if (JvmNameUtils.isSimpleName(name)) {
            Optional<TypeDefinition> res = resolver.findTypeDefinitionIn(qualifiedName.qualifiedName() + "." + name, this, resolver);
            if (res.isPresent()) {
                return Optional.of(res.get());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Node> getChildren() {
        return ImmutableList.of(qualifiedName);
    }
}
