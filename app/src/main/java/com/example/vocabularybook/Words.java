package com.example.vocabularybook;

import android.provider.BaseColumns;

public class Words {
    public static class WordItem {
        public String id;
        public String word;
        public WordItem(String id, String word) {
            this.id = id;
            this.word = word;
        }
        public String toString() {
            return word;
        }
    }
    public static class WordDescription {
        public String id;
        public String word;
        public String meaning;
        public String sample;
        public WordDescription(String id, String word,String meaning, String sample) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
            this.sample = sample;
        }

        public WordDescription() {

        }

        public String getId() {
            return id;
        }

        public String getWord() {
            return word;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getSample() {
            return sample;
        }

        public void setSample(String sample) {
            this.sample = sample;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }

        public void setWord(String word) {
            this.word = word;
        }
        public void setId(String id) {
            this.id = id;
        }
    }
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_MEANING = "meaning";
        public static final String COLUMN_NAME_SAMPLE = "sample";
    }
}
