package me.tomassetti.turin.parser;

import com.google.common.collect.ImmutableList;
import me.tomassetti.parser.antlr.TurinParser;
import me.tomassetti.turin.implicit.BasicTypeUsage;
import me.tomassetti.turin.parser.ast.*;
import me.tomassetti.turin.parser.ast.expressions.*;
import me.tomassetti.turin.parser.ast.expressions.literals.IntLiteral;
import me.tomassetti.turin.parser.ast.expressions.literals.StringLiteral;
import me.tomassetti.turin.parser.ast.statements.BlockStatement;
import me.tomassetti.turin.parser.ast.statements.ExpressionStatement;
import me.tomassetti.turin.parser.ast.statements.VariableDeclaration;
import me.tomassetti.turin.parser.ast.typeusage.ReferenceTypeUsage;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ParseTreeToAstTest {

    private TurinFile basicMangaAst() {
        TurinFile turinFile = new TurinFile();

        NamespaceDefinition namespaceDefinition = new NamespaceDefinition("manga");

        turinFile.setNameSpace(namespaceDefinition);

        ReferenceTypeUsage stringType = new ReferenceTypeUsage("String");
        BasicTypeUsage intType = BasicTypeUsage.UINT;

        PropertyDefinition nameProperty = new PropertyDefinition("name", stringType);

        turinFile.add(nameProperty);

        TurinTypeDefinition mangaCharacter = new TurinTypeDefinition("MangaCharacter");
        PropertyDefinition ageProperty = new PropertyDefinition("age", intType);
        PropertyReference nameRef = new PropertyReference("name");
        mangaCharacter.add(nameRef);
        mangaCharacter.add(ageProperty);

        turinFile.add(mangaCharacter);

        return turinFile;
    }

    private TurinFile mangaAst() {
        TurinFile turinFile = new TurinFile();

        NamespaceDefinition namespaceDefinition = new NamespaceDefinition("manga");

        turinFile.setNameSpace(namespaceDefinition);

        ReferenceTypeUsage stringType = new ReferenceTypeUsage("String");
        BasicTypeUsage intType = BasicTypeUsage.UINT;

        PropertyDefinition nameProperty = new PropertyDefinition("name", stringType);

        turinFile.add(nameProperty);

        TurinTypeDefinition mangaCharacter = new TurinTypeDefinition("MangaCharacter");
        PropertyDefinition ageProperty = new PropertyDefinition("age", intType);
        PropertyReference nameRef = new PropertyReference("name");
        mangaCharacter.add(nameRef);
        mangaCharacter.add(ageProperty);

        turinFile.add(mangaCharacter);


        // val ranma = MangaCharacter("Ranma", 16)
        Creation value = new Creation("MangaCharacter", ImmutableList.of(new ActualParam(new StringLiteral("Ranma")), new ActualParam(new IntLiteral(16))));
        VariableDeclaration varDecl = new VariableDeclaration("ranma", value);
        // print("The protagonist is #{ranma}")
        StringInterpolation string = new StringInterpolation();
        string.add(new StringLiteral("The protagonist is "));
        string.add(new ValueReference("ranma"));
        FunctionCall functionCall = new FunctionCall(new ValueReference("print"), ImmutableList.of(new ActualParam(string)));
        Program program = new Program("MangaExample", new BlockStatement(ImmutableList.of(varDecl, new ExpressionStatement(functionCall))), "args");
        turinFile.add(program);

        return turinFile;
    }

    @Test
    public void convertBasicMangaExample() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("basicManga.to");
        TurinParser.TurinFileContext root = new InternalParser().produceParseTree(inputStream);
        assertEquals(basicMangaAst(), new ParseTreeToAst().toAst(root));
    }

    @Test
    public void convertMangaExample() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("manga.to");
        TurinParser.TurinFileContext root = new InternalParser().produceParseTree(inputStream);
        assertEquals(mangaAst(), new ParseTreeToAst().toAst(root));
    }

}
