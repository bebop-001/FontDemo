/*
 *  Copyright 2018 Steven Smith kana-tutor.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *
 *  See the License for the specific language governing permissions
 *  and limitations under the License.
 */
package com.kana_tutor.fontdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontDemoActivity extends AppCompatActivity {
    private static final String TAG = FontDemoActivity.class.getSimpleName();

    private SharedPreferences myPrefs;
    private EditText fontEditText;
    private Button fontSelectButton;
    // prefs file for keeping the demo string
    private static final String myPrefsFileName = "myPrefs";
    // font resources to demo.
    static final Map<String, Integer> fontMap = new HashMap<>();
    static {
        fontMap.put("Takao", R.font.takao_gothic_regular);
        fontMap.put("HanaMinA", R.font.hana_min_a_regular);
        fontMap.put("DroidSansJapanese", R.font.droid_sans_japanese_regular);
    }
    static final List<String> fontNames = new ArrayList<>(fontMap.keySet());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.font_demo);
        myPrefs = getSharedPreferences(myPrefsFileName, Context.MODE_PRIVATE);
        String fontDemoString = myPrefs.getString("fontDemoString",
                "今日は皆さん。");
        fontEditText = findViewById(R.id.font_edit_text);
        fontEditText.setText(fontDemoString);
        fontSelectButton =  findViewById(R.id.font_select_button);
        fontSelectButton.setOnClickListener(new View.OnClickListener() {
            // toggle through the possible font selections.
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                String current = b.getText().toString();
                String next = null;
                for(int i = 0; i < fontNames.size() && next == null; i++) {
                    // match found.  Set font to next in the list.  Wrap...
                    if(current.equals(fontNames.get(i))) {
                        int idx = (i + 1) % fontNames.size();
                        next = fontNames.get(idx);
                    }
                }
                if (next == null)
                    next = fontNames.get(0);
                int fontResource = fontMap.get(next);
                Log.d(TAG, String.format("Font resource:0x%08x", fontResource));
                fontEditText.setTypeface(
                    ResourcesCompat.getFont(FontDemoActivity.this, fontResource)
                    , Typeface.BOLD);
                fontSelectButton.setText(next);
            }
        });
        fontEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            // update the demo string as the user changes it.
            @Override
            public void afterTextChanged(Editable s) {
                myPrefs.edit().putString("fontDemoString", s.toString()).apply();
            }
        });
    }
}
