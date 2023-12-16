package com.example.languagestransaltor.models;

import java.util.List;

public class TranslateData {
    private List<Translate> translations;

    public TranslateData(List<Translate> translations) {
        this.translations = translations;
    }

    public List<Translate> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translate> translations) {
        this.translations = translations;
    }
}
