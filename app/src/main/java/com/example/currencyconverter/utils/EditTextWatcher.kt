package com.example.currencyconverter.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class EditTextWatcher implements TextWatcher {
    private int lastProtectedPosition = 0;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Находим позицию первого пробела
        int spaceIndex = s.toString().indexOf(' ');
        lastProtectedPosition = spaceIndex >= 0 ? spaceIndex + 1 : 0;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Не требуется
    }

    @Override
    public void afterTextChanged(Editable s) {
        int spaceIndex = s.toString().indexOf(' ');
        int currentProtectedPos = spaceIndex >= 0 ? spaceIndex + 1 : 0;

        // Если защищенная область изменилась (пробел удален)
        if (currentProtectedPos < lastProtectedPosition) {
            // Отменяем изменения - возвращаем курсор в конец защищенной зоны
            Selection.setSelection(s, lastProtectedPosition);
        }

        lastProtectedPosition = currentProtectedPos;
    }
}