package me.tomassetti.turin.parser.ast;

import me.tomassetti.turin.implicit.BasicTypeUsage;
import me.tomassetti.turin.parser.Parser;
import me.tomassetti.turin.parser.analysis.resolvers.InFileResolver;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.expressions.ValueReference;
import me.tomassetti.turin.parser.ast.typeusage.ArrayTypeUsage;
import me.tomassetti.turin.parser.ast.typeusage.PrimitiveTypeUsage;
import me.tomassetti.turin.parser.ast.typeusage.ReferenceTypeUsage;
import me.tomassetti.turin.parser.ast.typeusage.TypeUsage;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class ValueReferenceTest {

    @Test
    public void solveReferenceToLocalVariable() throws IOException {
        String code = "namespace examples\n" +
                "program Example(String[] args) {\n" +
                "    val UInt a = 0\n" +
                "    a\n" +
                "}\n";
        InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        TurinFile turinFile = new Parser().parse(stream);
        List<ValueReference> valueReferences = turinFile.findAll(ValueReference.class);
        assertEquals(1, valueReferences.size());
        Resolver resolver = new InFileResolver();
        TypeUsage type = valueReferences.get(0).calcType(resolver);
        assertEquals(BasicTypeUsage.UINT, type);
    }

    @Test
    public void solveReferenceToProgramParam() throws IOException {
        String code = "namespace examples\n" +
                "program Example(String[] args) {\n" +
                "    args\n" +
                "}\n";
        InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        TurinFile turinFile = new Parser().parse(stream);
        List<ValueReference> valueReferences = turinFile.findAll(ValueReference.class);
        assertEquals(1, valueReferences.size());
        Resolver resolver = new InFileResolver();
        TypeUsage type = valueReferences.get(0).calcType(resolver);
        assertEquals(new ArrayTypeUsage(ReferenceTypeUsage.STRING), type);
    }

    @Test
    public void solveReferenceToMethodParam() throws IOException {
        String code = "namespace examples\n" +
                "type Example {\n" +
                "    Void foo(Int a) = a\n" +
                "}\n";
        InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        TurinFile turinFile = new Parser().parse(stream);
        List<ValueReference> valueReferences = turinFile.findAll(ValueReference.class);
        assertEquals(1, valueReferences.size());
        Resolver resolver = new InFileResolver();
        TypeUsage type = valueReferences.get(0).calcType(resolver);
        assertEquals(PrimitiveTypeUsage.INT, type);
    }

}
