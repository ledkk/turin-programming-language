import me.tomassetti.turin.analysis.InFileResolver;
import me.tomassetti.turin.analysis.Resolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by federico on 29/08/15.
 */
public class TypeDefinitionTest {

    private TypeDefinition mangaCharacter;

    @Before
    public void setup() {
        // define AST
        TurinFile turinFile = new TurinFile();

        NamespaceDefinition namespaceDefinition = new NamespaceDefinition("manga");

        turinFile.setNameSpace(namespaceDefinition);

        ReferenceTypeUsage stringType = new ReferenceTypeUsage("String");
        ReferenceTypeUsage intType = new ReferenceTypeUsage("UInt");

        PropertyDefinition nameProperty = new PropertyDefinition("name", stringType);

        turinFile.add(nameProperty);

        mangaCharacter = new TypeDefinition("MangaCharacter");
        PropertyDefinition ageProperty = new PropertyDefinition("age", intType);
        PropertyReference nameRef = new PropertyReference("name");
        mangaCharacter.add(nameRef);
        mangaCharacter.add(ageProperty);

        turinFile.add(mangaCharacter);
    }

    @Test
    public void getQualifiedName() {
        assertEquals("manga.MangaCharacter", mangaCharacter.getQualifiedName());
    }

    @Test
    public void getDirectProperties() {
        Resolver resolver = new InFileResolver();
        assertEquals(2, mangaCharacter.getDirectProperties(resolver).size());

        assertEquals("name", mangaCharacter.getDirectProperties(resolver).get(0).getName());
        assertEquals(new ReferenceTypeUsage("String"), mangaCharacter.getDirectProperties(resolver).get(0).getTypeUsage());

        assertEquals("age", mangaCharacter.getDirectProperties(resolver).get(1).getName());
        assertEquals(new ReferenceTypeUsage("UInt"), mangaCharacter.getDirectProperties(resolver).get(1).getTypeUsage());
    }

    @Test
    public void getDirectPropertiesOnRegistryExample() {
        Resolver resolver = new InFileResolver();
        TurinFile turinFile = ExamplesAst.registryAst();
        TypeDefinition person = turinFile.getTopTypeDefinition("Person").get();
        assertEquals(2, person.getDirectProperties(resolver).size());
    }

}
