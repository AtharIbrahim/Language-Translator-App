//
//
//
//            Develope By :) Athar Ibrahim Khalid
//
//            Published By :) Athar Ibrahim Khalid
//
//            See More Work On
//                -> Github: https://github.com/AtharIbrahim
//                -> Linkedin: https://www.linkedin.com/in/athar-ibrahim-khalid-0715172a2/
//                -> Dribbble: https://dribbble.com/AtharIbrahim
//
//  -This is the modern Language Translator App
//  -Concept is just like a simple
//
//
package com.example.langaugestranslator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;


import com.example.langaugestranslator.databinding.ActivityMainTranslatorBinding;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


import java.io.IOException;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Locale;


public class MainTranslator extends AppCompatActivity implements TextToSpeech.OnInitListener{
    ActivityMainTranslatorBinding binding;

    String[] fromLanguages = {"From" , "English", "Afrikanns", "Arabic", "Belarusian","Bulgarian", "Bengali", "Catalan","Czech","Welsh","Danish","German","Greek","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew", "Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukranian", "Urdu","Vietnamese"};
    String[] toLanguages = {"To" , "English", "Afrikanns", "Arabic", "Belarusian","Bulgarian", "Bengali", "Catalan","Czech","Welsh","Danish","German","Greek","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew", "Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukranian", "Urdu","Vietnamese"};

    private static final int REQUEST_PERMISSION_CODE = 1;
    int langauageCode, fromLanguageCode, toLanguageCode = 0;
    ProgressDialog progressDialog;
    Uri imageUri;
    private TextToSpeech textToSpeech;
    // Define SharedPreferences keys
    private static final String FROM_LANGUAGE_KEY = "from_language";
    private static final String TO_LANGUAGE_KEY = "to_language";

    // SharedPreferences object
    private SharedPreferences sharedPreferences;
    InterstitialAd minterstitialAd;
    public static final String REWARD_AD_UNIT_ID="Your Reward Ad ID";

    private RewardedAd mRewardedAd;

    public static final String TAG="MainTranslator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainTranslatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.TRANSPARENT); // Or any other desired color


        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load previously selected languages
        fromLanguageCode = sharedPreferences.getInt(FROM_LANGUAGE_KEY, 0);
        toLanguageCode = sharedPreferences.getInt(TO_LANGUAGE_KEY, 0);


        progressDialog = new ProgressDialog(MainTranslator.this);
        progressDialog.setTitle("Downloading Model Please Wait...");

        textToSpeech = new TextToSpeech(this, this);
        
//        //Banner Ads
//        MobileAds.initialize(this);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdLoaded();
//                Toast.makeText(MainTranslator.this, "Ads Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });




        // Mic Setup
        binding.Mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                // Get the selected language code from the "From" spinner
                int selectedLanguageCode = getLanguageCode(binding.FromSpinner.getSelectedItem().toString());

                // Set the language for speech recognition to the selected language
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLanguageLocale(selectedLanguageCode));

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak To Convert In Text");
                try {
                    startActivityForResult(intent, REQUEST_PERMISSION_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainTranslator.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Source Text Count and set other btn
        binding.Source.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                int wordCount = countWords(editable.toString());
                if (wordCount > 20) {
                    binding.Source.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22); // Set text size to 20sp
                }
                else if(wordCount > 100){
                    binding.Source.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19); // Set text size to 20sp
                }
                else {
                    // Set your default text size here
                    binding.Source.setTextSize(TypedValue.COMPLEX_UNIT_SP,24 /* Your default text size */);
                }

                if (TextUtils.isEmpty(editable)) {
                    binding.Mic.setVisibility(View.VISIBLE);
                    binding.TranslteBtn.setVisibility(View.INVISIBLE);
                    binding.AddBtn.setVisibility(View.INVISIBLE);
                    binding.Clearbtn.setVisibility(View.INVISIBLE);
                    binding.SourceOperations.setVisibility(View.GONE);
                    binding.AfterTranslation.setVisibility(View.GONE);
                } else {
                    binding.Mic.setVisibility(View.INVISIBLE);
                    binding.TranslteBtn.setVisibility(View.VISIBLE);
                    binding.AddBtn.setVisibility(View.VISIBLE);
                    binding.Clearbtn.setVisibility(View.VISIBLE);
                    binding.SourceOperations.setVisibility(View.GONE);
                    binding.AfterTranslation.setVisibility(View.GONE);

                }
            }

            // Method to count words
            private int countWords(String text) {
                String trimmed = text.trim();
                if (trimmed.isEmpty())
                    return 0;
                return trimmed.split("\\s+").length; // Split by whitespace
            }
        });




        //Back Arrow Btn
        binding.BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTranslator.this, MainActivity2.class);
                startActivity(intent);
                finishAffinity();
                IntersationAds();
            }
        });

        //Plus btn to clear the source Text
        binding.AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.Source.setText("");
                binding.Destination.setText("");
                IntersationAds();
            }

        });

        //Cross Btn to clear source text
        binding.Clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.Source.setText("");
                binding.Destination.setText("");

            }
        });
        
        //Copy Source Text Btn
        binding.CopyTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the Source EditText
                String textToCopy = binding.Source.getText().toString();

                // Copy the text to the clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
                clipboard.setPrimaryClip(clip);

                // Display a toast message indicating the text has been copied
                Toast.makeText(MainTranslator.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //swap Languages Btn
        binding.SwapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapLanguages();
            }
        });


        // Speaker Source Btn
        binding.SpeakTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToSpeak = binding.Source.getText().toString();
                // Check if textToSpeak is not empty
                if (!TextUtils.isEmpty(textToSpeak)) {
                    // Set the speech rate to make it slower (0.7 for 70% of normal speed)
                    textToSpeech.setSpeechRate(0.7f); // Adjust the value as needed

                    // Check if the desired language is supported
                    if (isLanguageSupported(textToSpeak)) {
                        // Speak the text with the specified language and speech rate
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    } else {
                        // If the language is not supported, set the language to English (fallback)
                        textToSpeech.setLanguage(Locale.ENGLISH);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    }
                } else {
                    // Show a toast message if the EditText is empty
                    Toast.makeText(MainTranslator.this, "No text to speak", Toast.LENGTH_SHORT).show();
                }
            }
            // Method to check if the desired language is supported
            private boolean isLanguageSupported(String textToSpeak) {
                // Check if the desired language is supported
                int result = textToSpeech.isLanguageAvailable(getLocaleFromText(textToSpeak));
                return result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE;
            }

            // Method to get the Locale from the text (assuming text is in format: "en-US", "fr-FR", etc.)
            private Locale getLocaleFromText(String textToSpeak) {
                String[] parts = textToSpeak.split("-");
                if (parts.length > 1) {
                    return new Locale(parts[0], parts[1]);
                } else {
                    return new Locale(parts[0]);
                }
            }
        });

        // Translate Button click listener
        binding.TranslteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                binding.Destination.setText("");
                if(binding.Source.getText().toString().isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(MainTranslator.this, "Please Enter Your Text To Translate...", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(MainTranslator.this, "Please Select Your Source Language...", Toast.LENGTH_SHORT).show();

                } else if (toLanguageCode==0) {
                    progressDialog.dismiss();
                    Toast.makeText(MainTranslator.this, "Please Select Language To Translation...", Toast.LENGTH_SHORT).show();
                }
                else{
                    translateText(fromLanguageCode,toLanguageCode,binding.Source.getText().toString());
                }


            }
        });


        // Set up spinner adapters
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguages);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguages);
// Set dropdown view resource
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to spinners
        binding.FromSpinner.setAdapter(fromAdapter);
        binding.ToSpinner.setAdapter(toAdapter);

        // Set selected items in spinners based on saved language codes
        binding.FromSpinner.setSelection(getLanguageIndex(fromLanguageCode, fromLanguages));
        binding.ToSpinner.setSelection(getLanguageIndex(toLanguageCode, toLanguages));

        // Spinner listeners...


        // From Language set
        binding.FromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
                // Save the selected languages to SharedPreferences
                saveSelectedLanguages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

// To Language Set
        binding.ToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
                // Save the selected languages to SharedPreferences
                saveSelectedLanguages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Copy Destination Text Btn
        binding.DestinationCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the Source EditText
                String textToCopy = binding.Destination.getText().toString();

                // Copy the text to the clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
                clipboard.setPrimaryClip(clip);

                // Display a toast message indicating the text has been copied
                Toast.makeText(MainTranslator.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        //Share Destination Text Btn
        binding.DestinationShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the Destination EditText
                String textToShare = binding.Destination.getText().toString();

                // Create an Intent to share text
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                shareIntent.setType("text/plain");

                // Start an activity to handle the Intent
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });


        //full Screen Destionation Btn
        binding.DestinationFullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.DestinationFullText.setVisibility(View.VISIBLE);
            }
        });


        //Destination Text Count and set other btn
        binding.Destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                int wordCount = countWords(editable.toString());
                if (wordCount > 20) {
                    binding.Destination.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22); // Set text size to 20sp
                }
                else if(wordCount > 100){
                    binding.Destination.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19); // Set text size to 20sp
                }
                else {
                    // Set your default text size here
                    binding.Destination.setTextSize(TypedValue.COMPLEX_UNIT_SP,24 /* Your default text size */);
                }

                if (TextUtils.isEmpty(editable)) {
                    binding.DestinationOperations.setVisibility(View.GONE);
                } else {
                    binding.DestinationOperations.setVisibility(View.VISIBLE);
                }
            }

            // Method to count words
            private int countWords(String text) {
                String trimmed = text.trim();
                if (trimmed.isEmpty())
                    return 0;
                return trimmed.split("\\s+").length; // Split by whitespace
            }
        });


        // Speaker Destination Btn
        binding.DestinationSpeakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToSpeak = binding.Destination.getText().toString();
                // Check if textToSpeak is not empty
                if (!TextUtils.isEmpty(textToSpeak)) {
                    // Set the speech rate to make it slower (0.7 for 70% of normal speed)
                    textToSpeech.setSpeechRate(0.7f); // Adjust the value as needed

                    // Check if the desired language is supported
                    if (isLanguageSupported(textToSpeak)) {
                        // Speak the text with the specified language and speech rate
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    } else {
                        // If the language is not supported, set the language to English (fallback)
                        textToSpeech.setLanguage(Locale.ENGLISH);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    }
                } else {
                    // Show a toast message if the EditText is empty
                    Toast.makeText(MainTranslator.this, "No text to speak", Toast.LENGTH_SHORT).show();
                }
            }
            // Method to check if the desired language is supported
            private boolean isLanguageSupported(String textToSpeak) {
                // Check if the desired language is supported
                int result = textToSpeech.isLanguageAvailable(getLocaleFromText(textToSpeak));
                return result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE;
            }

            // Method to get the Locale from the text (assuming text is in format: "en-US", "fr-FR", etc.)
            private Locale getLocaleFromText(String textToSpeak) {
                String[] parts = textToSpeak.split("-");
                if (parts.length > 1) {
                    return new Locale(parts[0], parts[1]);
                } else {
                    return new Locale(parts[0]);
                }
            }
        });


    }


    public void IntersationAds() {
        //Intersation Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(MainTranslator.this, getString(R.string.inter_ads_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
//                Toast.makeText(MainTranslator.this, "Ads Not Loaded", Toast.LENGTH_SHORT).show();
                Log.e("AdPending", "ad is not loaded yet!");
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                minterstitialAd = interstitialAd;

                minterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        minterstitialAd = null;
                    }
                });
            }
        });
        //Intersaxtion Ads End
        if (minterstitialAd != null) {
            minterstitialAd.show(MainTranslator.this);
        } else {
            Log.e("AdPending", "ad is not ready yet!");
        }
    }

    public void RewardUnitAds(){
        loadRewardedAds();
        if (mRewardedAd!=null)
        {
            mRewardedAd.show(MainTranslator.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int amount=rewardItem.getAmount();
                    String type=rewardItem.getType();
                }
            });
        }else {
//            Toast.makeText(MainTranslator.this, "Reward ads is not ready yet", Toast.LENGTH_SHORT).show();
            Log.e("AdPending", "reward ad is not ready yet!");
        }
    }

    private void loadRewardedAds()
    {

        AdRequest adRequest=new AdRequest.Builder().build();
        RewardedAd.load(this, REWARD_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {


                super.onAdFailedToLoad(loadAdError);
                mRewardedAd=null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd=rewardedAd;

                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });

            }
        });

    }



    //Mic Call
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(resultCode == RESULT_OK && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.Source.setText(result.get(0));
                IntersationAds();
            }
        }
    }

    // Method to get the locale from language code
    private String getLanguageLocale(int languageCode) {
        Locale locale;
        switch (languageCode) {
            case FirebaseTranslateLanguage.EN:
                locale = Locale.ENGLISH;
                break;
            case FirebaseTranslateLanguage.AF:
                locale = new Locale("af");
                break;
            case FirebaseTranslateLanguage.AR:
                locale = new Locale("ar");
                break;
            case FirebaseTranslateLanguage.BE:
                locale = new Locale("be");
                break;
            case FirebaseTranslateLanguage.BG:
                locale = new Locale("bg");
                break;
            case FirebaseTranslateLanguage.BN:
                locale = new Locale("bn");
                break;
            case FirebaseTranslateLanguage.CA:
                locale = new Locale("ca");
                break;
            case FirebaseTranslateLanguage.CS:
                locale = new Locale("cs");
                break;
            case FirebaseTranslateLanguage.CY:
                locale = new Locale("cy");
                break;
            case FirebaseTranslateLanguage.DA:
                locale = new Locale("da");
                break;
            case FirebaseTranslateLanguage.DE:
                locale = new Locale("de");
                break;
            case FirebaseTranslateLanguage.EL:
                locale = new Locale("el");
                break;
            case FirebaseTranslateLanguage.EO:
                locale = new Locale("eo");
                break;
            case FirebaseTranslateLanguage.ES:
                locale = new Locale("es");
                break;
            case FirebaseTranslateLanguage.ET:
                locale = new Locale("et");
                break;
            case FirebaseTranslateLanguage.FA:
                locale = new Locale("fa");
                break;
            case FirebaseTranslateLanguage.FI:
                locale = new Locale("fi");
                break;
            case FirebaseTranslateLanguage.FR:
                locale = new Locale("fr");
                break;
            case FirebaseTranslateLanguage.GA:
                locale = new Locale("ga");
                break;
            case FirebaseTranslateLanguage.GL:
                locale = new Locale("gl");
                break;
            case FirebaseTranslateLanguage.GU:
                locale = new Locale("gu");
                break;
            case FirebaseTranslateLanguage.HE:
                locale = new Locale("he");
                break;
            case FirebaseTranslateLanguage.HI:
                locale = new Locale("hi");
                break;
            case FirebaseTranslateLanguage.HR:
                locale = new Locale("hr");
                break;
            case FirebaseTranslateLanguage.HT:
                locale = new Locale("ht");
                break;
            case FirebaseTranslateLanguage.HU:
                locale = new Locale("hu");
                break;
            case FirebaseTranslateLanguage.ID:
                locale = new Locale("id");
                break;
            case FirebaseTranslateLanguage.IS:
                locale = new Locale("is");
                break;
            case FirebaseTranslateLanguage.IT:
                locale = new Locale("it");
                break;
            case FirebaseTranslateLanguage.JA:
                locale = new Locale("ja");
                break;
            case FirebaseTranslateLanguage.KA:
                locale = new Locale("ka");
                break;
            case FirebaseTranslateLanguage.KN:
                locale = new Locale("kn");
                break;
            case FirebaseTranslateLanguage.KO:
                locale = new Locale("ko");
                break;
            case FirebaseTranslateLanguage.LT:
                locale = new Locale("lt");
                break;
            case FirebaseTranslateLanguage.LV:
                locale = new Locale("lv");
                break;
            case FirebaseTranslateLanguage.MK:
                locale = new Locale("mk");
                break;
            case FirebaseTranslateLanguage.MR:
                locale = new Locale("mr");
                break;
            case FirebaseTranslateLanguage.MS:
                locale = new Locale("ms");
                break;
            case FirebaseTranslateLanguage.MT:
                locale = new Locale("mt");
                break;
            case FirebaseTranslateLanguage.NL:
                locale = new Locale("nl");
                break;
            case FirebaseTranslateLanguage.NO:
                locale = new Locale("no");
                break;
            case FirebaseTranslateLanguage.PL:
                locale = new Locale("pl");
                break;
            case FirebaseTranslateLanguage.PT:
                locale = new Locale("pt");
                break;
            case FirebaseTranslateLanguage.RO:
                locale = new Locale("ro");
                break;
            case FirebaseTranslateLanguage.RU:
                locale = new Locale("ru");
                break;
            case FirebaseTranslateLanguage.SK:
                locale = new Locale("sk");
                break;
            case FirebaseTranslateLanguage.SL:
                locale = new Locale("sl");
                break;
            case FirebaseTranslateLanguage.SQ:
                locale = new Locale("sq");
                break;
            case FirebaseTranslateLanguage.SV:
                locale = new Locale("sv");
                break;
            case FirebaseTranslateLanguage.SW:
                locale = new Locale("sw");
                break;
            case FirebaseTranslateLanguage.TA:
                locale = new Locale("ta");
                break;
            case FirebaseTranslateLanguage.TE:
                locale = new Locale("te");
                break;
            case FirebaseTranslateLanguage.TH:
                locale = new Locale("th");
                break;
            case FirebaseTranslateLanguage.TL:
                locale = new Locale("tl");
                break;
            case FirebaseTranslateLanguage.TR:
                locale = new Locale("tr");
                break;
            case FirebaseTranslateLanguage.UK:
                locale = new Locale("uk");
                break;
            case FirebaseTranslateLanguage.UR:
                locale = new Locale("ur");
                break;
            case FirebaseTranslateLanguage.VI:
                locale = new Locale("vi");
                break;
            // Add cases for other languages as needed
            default:
                locale = Locale.getDefault(); // Default to device's locale
                break;
        }
        return locale.toString();
    }

    // Implement TextToSpeech.OnInitListener method
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Language selection, if needed
            int result = textToSpeech.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown TextToSpeech when activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    private void translateText(int fromLanguageCode, int toLanguageCode, String source){
        progressDialog.show();
        progressDialog.setCancelable(false);
        binding.Destination.setText("Downloading Model...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(fromLanguageCode).setTargetLanguage(toLanguageCode).build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                binding.Destination.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        binding.Destination.setText(s);
                        RewardUnitAds();
                        binding.AfterTranslation.setVisibility(View.VISIBLE);
                        binding.SourceOperations.setVisibility(View.VISIBLE);
                        binding.DestinationOperations.setVisibility(View.VISIBLE);
                        binding.TranslteBtn.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainTranslator.this, "Fail To Translate : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainTranslator.this, "Fail To Download Language Model..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to get the index of a language code in the language array
    private int getLanguageIndex(int languageCode, String[] languages) {
        for (int i = 0; i < languages.length; i++) {
            if (getLanguageCode(languages[i]) == languageCode) {
                return i;
            }
        }
        return 0; // Default to the first language if not found
    }

    // Method to swap the selected "from" and "to" languages
    private void swapLanguages() {
        // Get the current selected languages
        int tempLanguageCode = fromLanguageCode;
        fromLanguageCode = toLanguageCode;
        toLanguageCode = tempLanguageCode;

        // Save the swapped languages to SharedPreferences
        sharedPreferences.edit().putInt(FROM_LANGUAGE_KEY, fromLanguageCode).apply();
        sharedPreferences.edit().putInt(TO_LANGUAGE_KEY, toLanguageCode).apply();

        // Update the spinners to reflect the swapped languages
        binding.FromSpinner.setSelection(getLanguageIndex(fromLanguageCode, fromLanguages));
        binding.ToSpinner.setSelection(getLanguageIndex(toLanguageCode, toLanguages));
    }

    // Inside MainActivity

    // Method to save the selected languages to SharedPreferences
    private void saveSelectedLanguages() {
        sharedPreferences.edit().putInt(FROM_LANGUAGE_KEY, fromLanguageCode).apply();
        sharedPreferences.edit().putInt(TO_LANGUAGE_KEY, toLanguageCode).apply();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Handle back button press
            playContent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playContent() {
        // Replace this with your content play logic
        // For example, opening a video player activity
        Intent intent = new Intent(MainTranslator.this, MainActivity2.class);
        startActivity(intent);
        finishAffinity();
    }


    //    String[] toLanguages = {"To" , "English", "Afrikanns", "Arabic", "Belarusian","Bulgarian", "Bengali", "Catalan",
    public int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language) {
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Afrikanns":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BG;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;


//    "Cezch","Czech","Welsh","Danish","German","Greek","Esperanto","Spanish","Estonian","Persian","Finnish","French",

            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "Danish":
                languageCode = FirebaseTranslateLanguage.DA;
                break;
            case "German":
                languageCode = FirebaseTranslateLanguage.DE;
                break;
            case "Greek":
                languageCode = FirebaseTranslateLanguage.EL;
                break;
            case "Esperanto":
                languageCode = FirebaseTranslateLanguage.EO;
                break;
            case "Spanish":
                languageCode = FirebaseTranslateLanguage.ES;
                break;
            case "Estonian":
                languageCode = FirebaseTranslateLanguage.ET;
                break;
            case "Persian":
                languageCode = FirebaseTranslateLanguage.FA;
                break;
            case "Finnish":
                languageCode = FirebaseTranslateLanguage.FI;
                break;
            case "French":
                languageCode = FirebaseTranslateLanguage.FR;
                break;


//    "Irish","Galician","Gujarati","Hebrew", "Hindi","Croatian","Haitian","Hungarian",

            case "Irish":
                languageCode = FirebaseTranslateLanguage.GA;
                break;
            case "Galician":
                languageCode = FirebaseTranslateLanguage.GL;
                break;
            case "Gujarati":
                languageCode = FirebaseTranslateLanguage.GU;
                break;
            case "Hebrew":
                languageCode = FirebaseTranslateLanguage.HE;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Croatian":
                languageCode = FirebaseTranslateLanguage.HR;
                break;
            case "Haitian":
                languageCode = FirebaseTranslateLanguage.HT;
                break;
            case "Hungarian":
                languageCode = FirebaseTranslateLanguage.HU;
                break;


//    "Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian",

            case "Indonesian":
                languageCode = FirebaseTranslateLanguage.ID;
                break;
            case "Icelandic":
                languageCode = FirebaseTranslateLanguage.IS;
                break;
            case "Italian":
                languageCode = FirebaseTranslateLanguage.IT;
                break;
            case "Japanese":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            case "Georgian":
                languageCode = FirebaseTranslateLanguage.KA;
                break;
            case "Kannada":
                languageCode = FirebaseTranslateLanguage.KN;
                break;
            case "Korean":
                languageCode = FirebaseTranslateLanguage.KO;
                break;
            case "Lithuanian":
                languageCode = FirebaseTranslateLanguage.LT;
                break;

//    "Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian",

            case "Latvian":
                languageCode = FirebaseTranslateLanguage.LV;
                break;
            case "Macedonian":
                languageCode = FirebaseTranslateLanguage.MK;
                break;
            case "Marathi":
                languageCode = FirebaseTranslateLanguage.MR;
                break;
            case "Malay":
                languageCode = FirebaseTranslateLanguage.MS;
                break;
            case "Maltese":
                languageCode = FirebaseTranslateLanguage.MT;
                break;
            case "Dutch":
                languageCode = FirebaseTranslateLanguage.NL;
                break;
            case "Norwegian":
                languageCode = FirebaseTranslateLanguage.NO;
                break;
            case "Polish":
                languageCode = FirebaseTranslateLanguage.PL;
                break;
            case "Portuguese":
                languageCode = FirebaseTranslateLanguage.PT;
                break;
            case "Romanian":
                languageCode = FirebaseTranslateLanguage.RO;
                break;

//    "Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukranian", "Urdu","Vietnamese"};

            case "Russian":
                languageCode = FirebaseTranslateLanguage.RU;
                break;
            case "Slovak":
                languageCode = FirebaseTranslateLanguage.SK;
                break;
            case "Slovenian":
                languageCode = FirebaseTranslateLanguage.SL;
                break;
            case "Albanian":
                languageCode = FirebaseTranslateLanguage.SQ;
                break;
            case "Swedish":
                languageCode = FirebaseTranslateLanguage.SV;
                break;
            case "Swahili":
                languageCode = FirebaseTranslateLanguage.SW;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                languageCode = FirebaseTranslateLanguage.TE;
                break;
            case "Thai":
                languageCode = FirebaseTranslateLanguage.TH;
                break;
            case "Tagalog":
                languageCode = FirebaseTranslateLanguage.TL;
                break;
            case "Turkish":
                languageCode = FirebaseTranslateLanguage.TR;
                break;
            case "Ukranian":
                languageCode = FirebaseTranslateLanguage.UK;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            case "Vietnamese":
                languageCode = FirebaseTranslateLanguage.VI;
                break;


            default:
                languageCode = 0;


        }
        return languageCode;
    }
}