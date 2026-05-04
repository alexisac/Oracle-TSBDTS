package org.example.demotsbdts;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoTsbdtsApplication {

    public static void main(String[] args) {
        String projectPath = "D:\\Master\\TSBDTS\\demoTSBDTS\\src\\main\\java\\org\\example\\demotsbdts\\sample";

        CodeParserService parserService = new CodeParserService();
        OracleCodeRepository repository = new OracleCodeRepository();

        try {
            System.out.println("incep parsarea proiectului: " + projectPath);

            List<CodeChunk> chunks = parserService.parseProject(projectPath);

            System.out.println("Numar total de metode extrase: " + chunks.size());

            for (CodeChunk chunk : chunks) {
                System.out.println("Metoda extrasa: " + chunk.getSymbolName());
            }

            System.out.println("Sterg datele vechi din tabela code_chunks...");
            repository.deleteAllChunks();

            System.out.println("Inserez metodele extrase in Oracle...");
            repository.saveAll(chunks);

            System.out.println("Metodele au fost salvate in tabela code_chunks.");

        } catch (Exception exception) {
            System.err.println("A aparut o eroare.");
            exception.printStackTrace();
        }
    }
}
