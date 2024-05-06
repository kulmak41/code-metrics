package org.example;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetricsVisitor extends VoidVisitorAdapter<Void> {
    private final Map<MethodDeclaration, Integer> methodComplexity;
    private static final String camelCasePattern = "[a-z][A-Za-z\\d]*";
    private int methodsNumber;
    private int namingViolations;

    public MetricsVisitor() {
        this.methodComplexity = new HashMap<>();
        this.methodsNumber = 0;
        this.namingViolations = 0;
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        int complexity = 0;
        complexity += md.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.ForEachStmt.class).size();
        methodComplexity.put(md, complexity);

        ++methodsNumber;
        if (!md.getNameAsString().matches(camelCasePattern)) {
            ++namingViolations;
        }

        super.visit(md, arg);
    }

    public List<MethodComplexity> highestComplexityMethods(int count) {
        return methodComplexity.entrySet()
                .stream()
                .map(x -> new MethodComplexity(x.getKey(), x.getValue()))
                .sorted(Comparator.comparing(x -> ((MethodComplexity)x).complexity()).reversed())
                .limit(count)
                .toList();
    }

    public int namingViolationsPercentage() {
        return (int)Math.round(100.0 * namingViolations / methodsNumber);
    }
}
