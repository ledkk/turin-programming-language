package me.tomassetti.turin.parser.ast.expressions;

import com.google.common.collect.ImmutableList;
import me.tomassetti.turin.parser.analysis.resolvers.Resolver;
import me.tomassetti.turin.parser.ast.Node;
import me.tomassetti.turin.parser.ast.typeusage.TypeUsage;

public class MathOperation extends Expression {
    private Expression left;
    private Expression right;
    private Operator operator;

    @Override
    public Iterable<Node> getChildren() {
        return ImmutableList.of(left, right);
    }

    @Override
    public TypeUsage calcType(Resolver resolver) {
        return left.calcType(resolver);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Operator getOperator() {
        return operator;
    }

    public enum Operator {
        MULTIPLICATION("*"),
        DIVISION("/"),
        SUM("+"),
        SUBTRACTION("-");

        private String symbol;

        private Operator(String symbol) {
            this.symbol = symbol;
        }

        public static Operator fromSymbol(String symbol) {
            for (Operator operator : Operator.values()) {
                if (operator.symbol.equals(symbol)) {
                    return operator;
                }
            }
            throw new IllegalArgumentException(symbol);
        }
    }

    public MathOperation(Operator operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.left.setParent(this);
        this.right = right;
        this.right.setParent(this);
    }

}
