package org.example.demotsbdts;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CodeParserService {
    public List<CodeChunk> parseProject(String projectPath) throws IOException {
        List<CodeChunk> chunks = new ArrayList<>();

        try (var paths = Files.walk(Path.of(projectPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            List<CodeChunk> fileChunks = parseJavaFile(path);
                            chunks.addAll(fileChunks);
                        } catch (Exception exception) {
                            System.err.println("Nu am putut parsa fișierul: " + path);
                            System.err.println("Motiv: " + exception.getMessage());
                        }
                    });
        }

        return chunks;
    }

    private List<CodeChunk> parseJavaFile(Path path) throws IOException {
        List<CodeChunk> chunks = new ArrayList<>();

        CompilationUnit compilationUnit = StaticJavaParser.parse(path);

        String packageName = compilationUnit
                .getPackageDeclaration()
                .map(packageDeclaration -> packageDeclaration.getName().asString())
                .orElse("");

        List<ClassOrInterfaceDeclaration> classes =
                compilationUnit.findAll(ClassOrInterfaceDeclaration.class);

        for (ClassOrInterfaceDeclaration classDeclaration : classes) {
            String className = classDeclaration.getNameAsString();

            List<MethodDeclaration> methods =
                    classDeclaration.findAll(MethodDeclaration.class);

            for (MethodDeclaration methodDeclaration : methods) {
                String methodName = methodDeclaration.getNameAsString();
                String symbolName = className + "." + methodName;

                String chunkText = buildChunkText(
                        packageName,
                        className,
                        methodDeclaration
                );

                CodeChunk chunk = new CodeChunk(
                        path.toString(),
                        packageName,
                        className,
                        symbolName,
                        "METHOD",
                        chunkText
                );

                chunks.add(chunk);
            }
        }

        return chunks;
    }

    private String buildChunkText(
            String packageName,
            String className,
            MethodDeclaration methodDeclaration
    ) {
        String methodName = methodDeclaration.getNameAsString();
        String signature = methodDeclaration.getDeclarationAsString(
                false,
                false,
                false
        );

        return """
                Package: %s
                Class: %s
                Method: %s
                Signature: %s
                
                Code:
                %s
                """.formatted(
                packageName,
                className,
                methodName,
                signature,
                methodDeclaration.toString()
        );
    }
}
