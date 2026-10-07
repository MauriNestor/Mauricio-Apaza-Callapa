package com.bnb_prueba_tecnica_mauricio_apaza.ui.common;

import android.text.InputFilter;
import android.text.Spanned;

public final class AlphanumericInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder filtered = new StringBuilder(end - start);
        boolean modified = false;
        for (int i = start; i < end; i++) {
            char character = source.charAt(i);
            if (isAsciiLetterOrDigit(character)) {
                filtered.append(character);
            } else {
                modified = true;
            }
        }
        return modified ? filtered : null;
    }

    private static boolean isAsciiLetterOrDigit(char character) {
        return (character >= 'A' && character <= 'Z')
                || (character >= 'a' && character <= 'z')
                || (character >= '0' && character <= '9');
    }
}
