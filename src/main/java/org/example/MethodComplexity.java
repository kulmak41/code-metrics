package org.example;

import com.github.javaparser.ast.body.MethodDeclaration;

public record MethodComplexity(MethodDeclaration method, Integer complexity) {}