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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue,DefaultLocale")
public class FontDemoActivity extends AppCompatActivity {
    private static final String TAG = FontDemoActivity.class.getSimpleName();

    private static class FontInfo {
        final String name;
        final int fontResourceId, fontStyle;
        private FontInfo(String name, int fontResourceId, int fontStyle) {
            this.name = name;
            this.fontResourceId = fontResourceId;
            this.fontStyle = fontStyle;
        }
    }
    private EditText fontEditText;
    private Button fontSelectButton;
    // prefs file for keeping the demo string
    private static final String myPrefsFileName = "myPrefs";
    private SharedPreferences myPrefs;
    // font resources to demo.
    private static final Map<String, FontInfo> fontMap = new HashMap<>();
    static {
        fontMap.put("Takao NORMAL", new FontInfo("Takao NORMAL",
            R.font.takao_gothic_regular, Typeface.NORMAL));
        fontMap.put("Takao BOLD", new FontInfo("Takao BOLD",
            R.font.takao_gothic_regular, Typeface.BOLD));
        fontMap.put("Takao ITALIC", new FontInfo("Takao ITALIC",
            R.font.takao_gothic_regular, Typeface.ITALIC));
        fontMap.put("HanaMinA NORMAL", new FontInfo("HanaMinA NORMAL",
            R.font.hana_min_a_regular, Typeface.NORMAL));
        fontMap.put("HanaMinA BOLD", new FontInfo("HanaMinA NORMAL",
            R.font.hana_min_a_regular, Typeface.BOLD));
        fontMap.put("HanaMinA ITALIC", new FontInfo("HanaMinA ITALIC",
            R.font.hana_min_a_regular, Typeface.ITALIC));
        fontMap.put("DroidSansJapanese NORMAL", new FontInfo("DroidSansJapanese NORMAL" ,
            R.font.droid_sans_japanese_regular, Typeface.NORMAL));
        fontMap.put("DroidSansJapanese BOLD", new FontInfo("DroidSansJapanese BOLD",
            R.font.droid_sans_japanese_regular, Typeface.BOLD));
        fontMap.put("DroidSansJapanese ITALIC", new FontInfo("DroidSansJapanese ITALIC",
                R.font.droid_sans_japanese_regular, Typeface.ITALIC));
        fontMap.put("Lobster NORMAL", new FontInfo("Lobster Normal",
                R.font.lobster, Typeface.NORMAL));
    }
    private static final List<String> fontNames;
    static {
        List <String>l = new ArrayList<>(fontMap.keySet());
        Collections.sort(l);
        fontNames = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.font_demo);
        myPrefs = getSharedPreferences(myPrefsFileName, Context.MODE_PRIVATE);
        String fontName = myPrefs.getString("currentFontName", fontNames.get(0));
        FontInfo currentFontInfo = fontMap.get(fontName);
        if (currentFontInfo == null) {
            currentFontInfo = fontMap.get(fontNames.get(0));
            myPrefs.edit().putString("currentFontName", currentFontInfo.name).apply();
        }
        String fontDemoString = myPrefs.getString("fontDemoString",
                "今日は皆さん。");
        fontEditText = findViewById(R.id.font_edit_text);
        fontEditText.setTypeface(
                ResourcesCompat.getFont(FontDemoActivity.this,
                    currentFontInfo.fontResourceId), currentFontInfo.fontStyle);
        fontEditText.setText(fontDemoString);
        fontSelectButton =  findViewById(R.id.font_select_button);
        fontSelectButton.setText(currentFontInfo.name);
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
                FontInfo info = fontMap.get(next);
                myPrefs.edit().putString("currentFontName", next).apply();
                Log.d(TAG, String.format(
                    "Font resource:0x%08x:%d", info.fontResourceId, info.fontStyle));
                fontEditText.setTypeface(
                    ResourcesCompat.getFont(FontDemoActivity.this
                    , info.fontResourceId), info.fontStyle);
                fontSelectButton.setText(next);
            }
        });
        fontEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
            }
            // update the demo string as the user changes it.
            @Override
            public void afterTextChanged(Editable s) {
                myPrefs.edit()
                    .putString("fontDemoString", s.toString()).apply();
            }
        });
    }
}
