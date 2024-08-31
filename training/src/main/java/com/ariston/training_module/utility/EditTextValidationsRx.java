package com.ariston.training_module.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.PublishSubject;

public class EditTextValidationsRx {

    private static final String TAG = "EditTextValidationsRx";
    public static Observable<String> getTextWatcherObservable(@NonNull final EditText editText) {

        final PublishSubject<String> subject = PublishSubject.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: "+s.toString());
                subject.onNext(s.toString());
            }
        });

        return subject;
    }

    public static void removeTextWathchers(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(null);
        }
    }


}
