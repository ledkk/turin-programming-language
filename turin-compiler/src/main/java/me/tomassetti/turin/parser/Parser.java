package me.tomassetti.turin.parser;

import com.google.common.collect.ImmutableList;
import me.tomassetti.turin.parser.ast.TurinFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Parser {

    private InternalParser internalParser = new InternalParser();

    public TurinFile parse(InputStream inputStream) throws IOException {
        return new ParseTreeToAst().toAst(internalParser.produceParseTree(inputStream));
    }

    public List<TurinFile> parseAllIn(File file) throws IOException {
        if (file.isFile()) {
            return ImmutableList.of(parse(new FileInputStream(file)));
        } else if (file.isDirectory()) {
            List<TurinFile> result = new ArrayList<>();
            for (File child : file.listFiles()) {
                result.addAll(parseAllIn(child));
            }
            return result;
        } else {
            throw new IllegalArgumentException(file.getPath());
        }
    }
}
