package org.example;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsVisitor extends VoidVisitorAdapter<Void> {
    private Map<MethodDeclaration, Integer> complexityCounter;

    public MetricsVisitor() {
        this.complexityCounter = new HashMap<>();
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        int complexity = 0;
        complexity += md.findAll(com.github.javaparser.ast.stmt.IfStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.SwitchStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.ForStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.WhileStmt.class).size();
        complexity += md.findAll(com.github.javaparser.ast.stmt.ForEachStmt.class).size();

        complexityCounter.put(md, complexity);

        super.visit(md, arg);
    }

    public List<String> worstComplexityMethods() {
        return complexityCounter.entrySet()
                .stream()
                .sorted(Comparator.comparing(x -> ((Map.Entry<MethodDeclaration, Integer>) x).getValue()).reversed())
                .limit(3)
                .map(x -> x.getKey().getNameAsString())
                .toList();
    }

}
