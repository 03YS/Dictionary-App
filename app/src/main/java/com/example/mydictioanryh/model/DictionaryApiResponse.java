package com.example.mydictioanryh.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DictionaryApiResponse {
    @SerializedName("word")
    private String word;

    @SerializedName("meanings")
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public static class Meaning {
        @SerializedName("partOfSpeech")
        private String partOfSpeech;

        @SerializedName("definitions")
        private List<Definition> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }

        public static class Definition {
            @SerializedName("definition")
            private String definition;

            public String getDefinition() {
                return definition;
            }
        }
    }
}
