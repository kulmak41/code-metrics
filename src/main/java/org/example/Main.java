package org.example;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
//            String dirPath = args[0];
            String dirPath = "//Users//kulmak41//Dev//code-metrics//";
            SourceRoot root = new SourceRoot(Paths.get(dirPath));
            List<ParseResult<CompilationUnit>> results = root.tryToParse();
            MetricsVisitor visitor = new MetricsVisitor();
            for (var result : results) {
                if (result.isSuccessful()) {
                    result.getResult().get().accept(visitor, null);
                }
            }
            List<String> worstComplexityMethods = visitor.worstComplexityMethods();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}