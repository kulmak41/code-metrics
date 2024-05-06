package org.example;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void print(List<MethodComplexity> methods) {
        for (MethodComplexity methodComplexity : methods) {
            MethodDeclaration md = methodComplexity.method();
            ClassOrInterfaceDeclaration mdClass = (ClassOrInterfaceDeclaration)md.getParentNode().get();
            System.out.println(mdClass.getName() + "." + md.getNameAsString() + ": " + methodComplexity.complexity());
        }
    }

    public static void main(String[] args) {
        try {
//            String dirPath = args[0];
            String dirPath = "//Users//kulmak41//Dev//code-metrics//";
            SourceRoot root = new SourceRoot(Path.of(dirPath));
            List<ParseResult<CompilationUnit>> results = root.tryToParse();
            MetricsVisitor visitor = new MetricsVisitor();
            for (var result : results) {
                if (result.isSuccessful()) {
                    result.getResult().get().accept(visitor, null);
                }
            }

            List<MethodComplexity> highestComplexityMethods = visitor.highestComplexityMethods();

            System.out.println("Three methods with the highest complexity scores:");
            print(highestComplexityMethods);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}