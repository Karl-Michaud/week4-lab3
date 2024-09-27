package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {

    private final Map<String, String> codeMap = new HashMap<>();

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            String deleteLine = "ISO Language Names  Code";
            lines.remove(deleteLine);
            for (String line : lines) {
                String[] refactoredLine = line.split("\t");
                String key = refactoredLine[1];
                String value = refactoredLine[0];
                codeMap.put(key, value);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the language for the given language code.
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        String refactoredCode = code.toLowerCase();
        if (codeMap.containsKey(refactoredCode)) {
            return codeMap.get(refactoredCode);
        }
        return "";
    }

    /**
     * Returns the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            String code = entry.getKey();
            String languageCode = entry.getValue();
            if (language.equals(languageCode)) {
                return code;
            }
        }
        return "";
    }

    /**
     * Returns how many languages are included in this code converter.
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return codeMap.size();
    }
}
