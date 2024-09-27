package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private final Map<String, String> codeMap = new HashMap<>();
    private final Map<String, String[]> countryMap = new HashMap<>();

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        String firstLineCode = "Alpha-3 code";
        String firstLineCountry = "Country";
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            for (String line : lines) {
                String[] refactoredLine = line.split("\t");
                String keyCodeMap = refactoredLine[2];
                String country = refactoredLine[0];
                codeMap.put(keyCodeMap, country);
                countryMap.put(country, refactoredLine);

            }
            codeMap.remove(firstLineCode);
            countryMap.remove(firstLineCountry);
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        String refactoredCode = code.toUpperCase();
        if (codeMap.containsKey(refactoredCode)) {
            return codeMap.get(refactoredCode);
        }
        else {
            return "";
        }
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        if (countryMap.containsKey(country)) {
            return countryMap.get(country)[2];
        }
        else {
            return "";
        }

    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return countryMap.size();
    }
}
