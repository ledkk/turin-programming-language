import me.tomassetti.turin.*;
import me.tomassetti.turin.compiler.ClassFileDefinition;
import me.tomassetti.turin.compiler.Compiler;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by federico on 29/08/15.
 */
public class CompilerTest {

    private TurinFile mangaAst() {
        // define AST
        TurinFile turinFile = new TurinFile();

        NamespaceDefinition namespaceDefinition = new NamespaceDefinition("manga");

        turinFile.setNameSpace(namespaceDefinition);

        ReferenceTypeUsage stringType = new ReferenceTypeUsage("String");
        ReferenceTypeUsage intType = new ReferenceTypeUsage("UInt");

        PropertyDefinition nameProperty = new PropertyDefinition("name", stringType);

        turinFile.add(nameProperty);

        TypeDefinition mangaCharacter = new TypeDefinition("MangaCharacter");
        PropertyDefinition ageProperty = new PropertyDefinition("age", intType);
        PropertyReference nameRef = new PropertyReference("name");
        mangaCharacter.add(nameRef);
        mangaCharacter.add(ageProperty);

        turinFile.add(mangaCharacter);
        return turinFile;
    }


    @Test
    public void compileAstManga() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        TurinFile turinFile = mangaAst();

        // generate bytecode
        Compiler instance = new Compiler();
        List<ClassFileDefinition> classFileDefinitions = instance.compile(turinFile);
        assertEquals(1, classFileDefinitions.size());

        TurinClassLoader turinClassLoader = new TurinClassLoader();
        Class mangaCharacterClass = turinClassLoader.addClass(classFileDefinitions.get(0).getName(),
                classFileDefinitions.get(0).getBytecode());
        assertEquals(1, mangaCharacterClass.getConstructors().length);
        Object ranma = mangaCharacterClass.getConstructors()[0].newInstance("Ranma", 16);

        Method getName = mangaCharacterClass.getMethod("getName");
        assertEquals("Ranma", getName.invoke(ranma));

        Method getAge = mangaCharacterClass.getMethod("getAge");
        assertEquals(16, getAge.invoke(ranma));
    }

    @Test
    public void compileAstRegistryPerson() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        TurinFile turinFile = ExamplesAst.registryAst();

        // generate bytecode
        Compiler instance = new Compiler();
        List<ClassFileDefinition> classFileDefinitions = instance.compile(turinFile);
        assertEquals(2, classFileDefinitions.size());

        assertEquals("registry.Person", classFileDefinitions.get(0).getName());

        TurinClassLoader turinClassLoader = new TurinClassLoader();
        Class personClass = turinClassLoader.addClass(classFileDefinitions.get(0).getName(),
                classFileDefinitions.get(0).getBytecode());
        assertEquals(1, personClass.getConstructors().length);
        assertEquals(2, personClass.getConstructors()[0].getParameterTypes().length);
        Object federico = personClass.getConstructors()[0].newInstance("Federico", "Tomassetti");

        Method getFirstName = personClass.getMethod("getFirstName");
        assertEquals("Federico", getFirstName.invoke(federico));

        Method getLastName = personClass.getMethod("getLastName");
        assertEquals("Tomassetti", getLastName.invoke(federico));
    }

    @Test
    public void compileAstRegistryAddress() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        TurinFile turinFile = ExamplesAst.registryAst();

        // generate bytecode
        Compiler instance = new Compiler();
        List<ClassFileDefinition> classFileDefinitions = instance.compile(turinFile);
        assertEquals(2, classFileDefinitions.size());

        assertEquals("registry.Address", classFileDefinitions.get(1).getName());

        TurinClassLoader turinClassLoader = new TurinClassLoader();
        Class addressClass = turinClassLoader.addClass(classFileDefinitions.get(1).getName(),
                classFileDefinitions.get(1).getBytecode());
        assertEquals(1, addressClass.getConstructors().length);
        assertEquals(4, addressClass.getConstructors()[0].getParameterTypes().length);
        Object address = addressClass.getConstructors()[0].newInstance("Rue de Seze", 86, "Lyon", 69006);

        assertEquals("Rue de Seze", addressClass.getMethod("getStreet").invoke(address));
        assertEquals(86, addressClass.getMethod("getNumber").invoke(address));
        assertEquals("Lyon", addressClass.getMethod("getCity").invoke(address));
        assertEquals(69006, addressClass.getMethod("getZip").invoke(address));
    }

    @Test(expected = InvocationTargetException.class)
    public void nullIsNotAcceptedForNameProperty() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        TurinFile turinFile = mangaAst();

        // generate bytecode
        Compiler instance = new Compiler();
        List<ClassFileDefinition> classFileDefinitions = instance.compile(turinFile);
        assertEquals(1, classFileDefinitions.size());

        TurinClassLoader turinClassLoader = new TurinClassLoader();
        Class mangaCharacterClass = turinClassLoader.addClass(classFileDefinitions.get(0).getName(),
                classFileDefinitions.get(0).getBytecode());
        assertEquals(1, mangaCharacterClass.getConstructors().length);
        Object ranma = mangaCharacterClass.getConstructors()[0].newInstance(null, 16);
    }

    @Test(expected = InvocationTargetException.class)
    public void negativeAgeIsNotAcceptedForNameProperty() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        TurinFile turinFile = mangaAst();

        // generate bytecode
        Compiler instance = new Compiler();
        List<ClassFileDefinition> classFileDefinitions = instance.compile(turinFile);
        assertEquals(1, classFileDefinitions.size());

        TurinClassLoader turinClassLoader = new TurinClassLoader();
        Class mangaCharacterClass = turinClassLoader.addClass(classFileDefinitions.get(0).getName(),
                classFileDefinitions.get(0).getBytecode());
        assertEquals(1, mangaCharacterClass.getConstructors().length);
        Object ranma = mangaCharacterClass.getConstructors()[0].newInstance("Ranma", -16);
    }

}
