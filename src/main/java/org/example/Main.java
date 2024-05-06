package org.example;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void print(List<MethodComplexity> methods) {
        for (MethodComplexity methodComplexity : methods) {
            MethodDeclaration md = methodComplexity.method();
            ClassOrInterfaceDeclaration mdClass = (ClassOrInterfaceDeclaration) md.getParentNode().get();
            System.out.println(mdClass.getName() + "." + md.getNameAsString() + ": " + methodComplexity.complexity());
        }
    }

    public static void analyseFiles(List<ParseResult<CompilationUnit>> parseResults) {
        MetricsVisitor visitor = new MetricsVisitor();
        for (var result : parseResults) {
            if (result.isSuccessful() && result.getResult().isPresent()) {
                result.getResult().get().accept(visitor, null);
            }
        }

        List<MethodComplexity> highestComplexityMethods = visitor.highestComplexityMethods(3);

        System.out.println("Three methods with the highest complexity scores:");
        print(highestComplexityMethods);
        System.out.println();
        System.out.println(visitor.namingViolationsPercentage() + "% of the methods do not adhere to the camel case naming convention.");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Provide the path to the directory.");
            return;
        }

        Path dirPath = Path.of(args[0]);
        if (!dirPath.isAbsolute()) {
            dirPath = dirPath.toAbsolutePath();
        }

        if (!Files.isDirectory(dirPath)) {
            System.out.println("The given path should be a directory.");
            return;
        }

        try {
            SourceRoot root = new SourceRoot(dirPath);
            List<ParseResult<CompilationUnit>> parseResults = root.tryToParse();
            analyseFiles(parseResults);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}