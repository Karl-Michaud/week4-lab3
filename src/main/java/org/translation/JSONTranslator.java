package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final JSONArray dataJsonArray;
    private final List<String> allCountryCodes = new ArrayList<>();
    private final Map<String, Map<String, String>> codeMap = new HashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        final String alpha2 = "alpha2";
        final String alpha3 = "alpha3";
        final String id = "id";
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            this.dataJsonArray = jsonArray;

            for (int i = 0; i < this.dataJsonArray.length(); i++) {
                Map<String, String> languageToTranslation = new HashMap<>();
                JSONObject lineObject = this.dataJsonArray.getJSONObject(i);
                String countryCode = new String(lineObject.getString(alpha3));
                if (!" ".equals(countryCode)) {
                    allCountryCodes.add(countryCode);
                }
                for (String languageCode : lineObject.keySet()) {
                    if (!languageCode.equals(alpha3) && !languageCode.equals(alpha2) && !languageCode.equals(id)) {
                        String notAliasCode = new String(languageCode);
                        String notAliasTranslation = new String(lineObject.getString(languageCode));
                        languageToTranslation.put(notAliasCode, notAliasTranslation);
                    }
                }
                codeMap.put(countryCode, languageToTranslation);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        String refactoredCountry = country.toLowerCase();
        if (codeMap.containsKey(refactoredCountry)) {
            List<String> languages = new ArrayList<>();
            for (String lang : codeMap.get(refactoredCountry).keySet()) {
                languages.add(lang);
            }
            return languages;
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        return this.allCountryCodes;
    }

    @Override
    public String translate(String country, String language) {
        String refactoredCountry = country.toLowerCase();
        if (codeMap.containsKey(refactoredCountry)) {
            String refactoredLanguage = language.toLowerCase();
            if (codeMap.get(refactoredCountry).containsKey(refactoredLanguage)) {
                return codeMap.get(refactoredCountry).get(refactoredLanguage);
            }
        }
        return null;
    }
}
