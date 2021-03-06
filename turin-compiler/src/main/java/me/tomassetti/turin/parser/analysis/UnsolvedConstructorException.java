package me.tomassetti.turin.parser.analysis;

import me.tomassetti.turin.parser.ast.expressions.ActualParam;
import me.tomassetti.turin.parser.ast.typeusage.TypeUsage;

import java.util.List;

public class UnsolvedConstructorException extends UnsolvedException {

    private String typeCanonicalName;
    private List<ActualParam> paramList;

    public UnsolvedConstructorException(String typeCanonicalName, List<ActualParam> paramList) {
        super("Unsolved constructor for " + typeCanonicalName + " with params " + paramList);
        this.typeCanonicalName = typeCanonicalName;
        this.paramList = paramList;
    }
}
