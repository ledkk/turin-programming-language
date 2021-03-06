package me.tomassetti.turin.parser.ast.reflection;

import me.tomassetti.turin.parser.analysis.resolvers.InFileResolver;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.TypeDefinition;
import me.tomassetti.turin.parser.ast.typeusage.ReferenceTypeUsage;
import org.junit.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionBasedTypeDefinitionTest {

    @Test
    public void getAllAncestorsOfString(){
        Resolver resolver = new InFileResolver();
        TypeDefinition typeDefinition = ReflectionTypeDefinitionFactory.getInstance().getTypeDefinition(String.class);
        List<ReferenceTypeUsage> ancestors = typeDefinition.getAllAncestors(resolver);
        assertEquals(4, ancestors.size());
        Set<String> names = ancestors.stream().map((a)->a.asReferenceTypeUsage().getQualifiedName(resolver)).collect(Collectors.toSet());
        assertTrue(names.contains(Object.class.getCanonicalName()));
        assertTrue(names.contains(Serializable.class.getCanonicalName()));
        assertTrue(names.contains(CharSequence.class.getCanonicalName()));
        assertTrue(names.contains(Comparable.class.getCanonicalName()));
        for (ReferenceTypeUsage ancestor : ancestors) {
            if (ancestor.getQualifiedName(resolver).equals(Comparable.class.getCanonicalName())) {
                assertEquals(1, ancestor.getTypeParameterValues().getInOrder().size());
                assertEquals("java.lang.String", ancestor.getTypeParameterValues().getInOrder().get(0).asReferenceTypeUsage().getQualifiedName(resolver));
            }
        }
    }

    @Test
    public void getAllAncestorsOfObject(){
        Resolver resolver = new InFileResolver();
        TypeDefinition typeDefinition = ReflectionTypeDefinitionFactory.getInstance().getTypeDefinition(Object.class);
        List<ReferenceTypeUsage> ancestors = typeDefinition.getAllAncestors(resolver);
        assertEquals(0, ancestors.size());
    }

    @Test
    public void getAllAncestorsOfSerializable(){
        Resolver resolver = new InFileResolver();
        TypeDefinition typeDefinition = ReflectionTypeDefinitionFactory.getInstance().getTypeDefinition(Object.class);
        List<ReferenceTypeUsage> ancestors = typeDefinition.getAllAncestors(resolver);
        assertEquals(0, ancestors.size());
    }

    @Test
    public void isInterfaceNegativeCase() {
        TypeDefinition typeDefinition = ReflectionTypeDefinitionFactory.getInstance().getTypeDefinition(String.class);
        assertEquals(false, typeDefinition.isInterface());
    }

    @Test
    public void isInterfacePositiveCase() {
        TypeDefinition typeDefinition = ReflectionTypeDefinitionFactory.getInstance().getTypeDefinition(List.class);
        assertEquals(true, typeDefinition.isInterface());
    }

}
