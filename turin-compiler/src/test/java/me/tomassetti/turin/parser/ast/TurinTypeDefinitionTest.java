package me.tomassetti.turin.parser.ast;

import com.google.common.collect.ImmutableList;
import me.tomassetti.turin.parser.Parser;
import me.tomassetti.turin.parser.analysis.UnsolvedConstructorException;
import me.tomassetti.turin.parser.analysis.resolvers.InFileResolver;
import me.tomassetti.turin.jvm.JvmConstructorDefinition;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.expressions.ActualParam;
import me.tomassetti.turin.parser.ast.expressions.literals.BooleanLiteral;
import me.tomassetti.turin.parser.ast.expressions.literals.FloatLiteral;
import me.tomassetti.turin.parser.ast.typeusage.PrimitiveTypeUsage;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;

public class TurinTypeDefinitionTest {

    @Test
    public void getQualifiedName() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        turinFile.add(typeDefinition);
        assertEquals("me.tomassetti.MyType", typeDefinition.getQualifiedName());
    }

    @Test
    public void resolveConstructorCallForTypeWithoutPropertiesWhenInvokedWithoutArguments() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, Collections.emptyList());
        assertEquals("me/tomassetti/MyType", constructor.getOwnerInternalName());
        assertEquals("<init>", constructor.getName());
        assertEquals("()V", constructor.getDescriptor());
    }

    @Test(expected = UnsolvedConstructorException.class)
    public void resolveConstructorCallForTypeWithoutPropertiesWhenInvokedWithTooManyArguments() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam(new BooleanLiteral(false));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
    }

    @Test
    public void resolveConstructorCallForTypeWithOnePropertyInvokedWithOneParameterOfCorrectType() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        PropertyDefinition propertyDefinition = new PropertyDefinition("coefficient", PrimitiveTypeUsage.FLOAT);
        typeDefinition.add(propertyDefinition);
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam(new FloatLiteral(0.0f));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
        assertEquals("me/tomassetti/MyType", constructor.getOwnerInternalName());
        assertEquals("<init>", constructor.getName());
        assertEquals("(F)V", constructor.getDescriptor());
    }

    @Test(expected = UnsolvedConstructorException.class)
    public void resolveConstructorCallForTypeWithOnePropertyInvokedWithOneParameterOfWrongType() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        PropertyDefinition propertyDefinition = new PropertyDefinition("coefficient", PrimitiveTypeUsage.FLOAT);
        typeDefinition.add(propertyDefinition);
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam(new BooleanLiteral(false));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
    }

    @Test(expected = UnsolvedConstructorException.class)
    public void resolveConstructorCallForTypeWithOnePropertyInvokedWithNamedParameterWithCorrectNameButWrongType() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        PropertyDefinition propertyDefinition = new PropertyDefinition("coefficient", PrimitiveTypeUsage.FLOAT);
        typeDefinition.add(propertyDefinition);
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam("coefficient", new BooleanLiteral(false));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
        assertEquals("me/tomassetti/MyType", constructor.getOwnerInternalName());
        assertEquals("<init>", constructor.getName());
        assertEquals("()V", constructor.getDescriptor());
    }

    @Test
    public void resolveConstructorCallForTypeWithOnePropertyInvokedWithNamedParameterWithCorrectNameAndCorrectType() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        PropertyDefinition propertyDefinition = new PropertyDefinition("coefficient", PrimitiveTypeUsage.FLOAT);
        typeDefinition.add(propertyDefinition);
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam("coefficient", new FloatLiteral(0.0f));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
        assertEquals("me/tomassetti/MyType", constructor.getOwnerInternalName());
        assertEquals("<init>", constructor.getName());
        assertEquals("(F)V", constructor.getDescriptor());
    }

    @Test(expected = UnsolvedConstructorException.class)
    public void resolveConstructorCallForTypeWithOnePropertyInvokedWithNamedParameterWithWrongName() {
        TurinFile turinFile = new TurinFile();
        NamespaceDefinition namespace = new NamespaceDefinition("me.tomassetti");
        turinFile.setNameSpace(namespace);
        TurinTypeDefinition typeDefinition = new TurinTypeDefinition("MyType");
        turinFile.add(typeDefinition);

        Resolver resolver = new InFileResolver();

        ActualParam p0 = new ActualParam("zzz", new FloatLiteral(0.0f));
        JvmConstructorDefinition constructor = typeDefinition.resolveConstructorCall(resolver, ImmutableList.of(p0));
    }

    @Test
    public void defineMethodToString() throws IOException {
        TurinFile turinFile = new Parser().parse(this.getClass().getResourceAsStream("/common_methods.to"));
        Resolver resolver = new InFileResolver();
        assertEquals(true, turinFile.getTopTypeDefinition("A").get().defineMethodToString(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("B").get().defineMethodToString(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("C").get().defineMethodToString(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("D").get().defineMethodToString(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("E").get().defineMethodToString(resolver));
    }

    @Test
    public void defineMethodHashCode() throws IOException {
        TurinFile turinFile = new Parser().parse(this.getClass().getResourceAsStream("/common_methods.to"));
        Resolver resolver = new InFileResolver();
        assertEquals(false, turinFile.getTopTypeDefinition("A").get().defineMethodHashCode(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("B").get().defineMethodHashCode(resolver));
        assertEquals(true, turinFile.getTopTypeDefinition("C").get().defineMethodHashCode(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("D").get().defineMethodHashCode(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("E").get().defineMethodHashCode(resolver));
    }

    @Test
    public void defineMethodEquals() throws IOException {
        TurinFile turinFile = new Parser().parse(this.getClass().getResourceAsStream("/common_methods.to"));
        Resolver resolver = new InFileResolver();
        assertEquals(false, turinFile.getTopTypeDefinition("A").get().defineMethodEquals(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("B").get().defineMethodEquals(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("C").get().defineMethodEquals(resolver));
        assertEquals(true, turinFile.getTopTypeDefinition("D").get().defineMethodEquals(resolver));
        assertEquals(false, turinFile.getTopTypeDefinition("E").get().defineMethodEquals(resolver));
    }

}
