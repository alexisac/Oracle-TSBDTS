package org.example.demotsbdts;

public class CodeChunk {
    private final String filePath;
    private final String packageName;
    private final String className;
    private final String symbolName;
    private final String chunkType;
    private final String sourceCode;

    public CodeChunk(
            String filePath,
            String packageName,
            String className,
            String symbolName,
            String chunkType,
            String sourceCode
    ) {
        this.filePath = filePath;
        this.packageName = packageName;
        this.className = className;
        this.symbolName = symbolName;
        this.chunkType = chunkType;
        this.sourceCode = sourceCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getChunkType() {
        return chunkType;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        return """
                ------------------------------
                File: %s
                Package: %s
                Class: %s
                Symbol: %s
                Type: %s
                
                Source:
                %s
                """.formatted(
                filePath,
                packageName,
                className,
                symbolName,
                chunkType,
                sourceCode
        );
    }
}
