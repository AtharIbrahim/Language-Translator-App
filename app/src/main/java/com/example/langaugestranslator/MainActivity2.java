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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langaugestranslator.databinding.ActivityMain2Binding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;
    ImageView drawerBtn;
    String[] fromLanguages = {"From" , "English", "Afrikanns", "Arabic", "Belarusian","Bulgarian", "Bengali", "Catalan","Czech","Welsh","Danish","German","Greek","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew", "Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukranian", "Urdu","Vietnamese"};
    String[] toLanguages = {"To" , "English", "Afrikanns", "Arabic", "Belarusian","Bulgarian", "Bengali", "Catalan","Czech","Welsh","Danish","German","Greek","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew", "Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukranian", "Urdu","Vietnamese"};

    private static final int REQUEST_PERMISSION_CODE = 1;
    int langauageCode, fromLanguageCode, toLanguageCode = 0;

    private static final String FROM_LANGUAGE_KEY = "from_language";
    private static final String TO_LANGUAGE_KEY = "to_language";

    // SharedPreferences object
    private SharedPreferences sharedPreferences;

    AdView adView;
    CardView CameraBtn, MicBtn, conversationbtn, historybtn, primiumbtn;
    Spinner fromspinner, tospinner;
    EditText sourcetext;
    ImageView swapbtn;

    Dialog mydialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adView = findViewById(R.id.adView2);
        CameraBtn = findViewById(R.id.CameraBtn);
        MicBtn = findViewById(R.id.MicBtn);
        conversationbtn = findViewById(R.id.ConversationBtn);
        historybtn = findViewById(R.id.HistoryBtn);
        primiumbtn = findViewById(R.id.PrimiumBtn);
        fromspinner = findViewById(R.id.idFromSpinner);
        tospinner = findViewById(R.id.idToSpinner);
        swapbtn = findViewById(R.id.SwapBtn);
        sourcetext = findViewById(R.id.SourceText);
        drawerBtn = findViewById(R.id.menuIcon);

        mydialog = new Dialog(this);

        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.open();
            }
        });

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navLanguages){
                    Toast.makeText(MainActivity2.this, "Languages is Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.navPrivacy){
                    Toast.makeText(MainActivity2.this, "Privacy is Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.navRateUs){
                    Toast.makeText(MainActivity2.this, "Rate Us is Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.navShareApp){
                    // Create an intent to share the app concept
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Concept");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app concept!");
                    startActivity(Intent.createChooser(shareIntent, "Share via"));

                    Toast.makeText(MainActivity2.this, "Share is Clicked", Toast.LENGTH_SHORT).show();
                }
//                binding.drawerLayout.close();

                return false;
            }
        });


        //Ads
//        MobileAds.initialize(this);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView2.loadAd(adRequest);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdLoaded();
                Toast.makeText(MainActivity2.this, "Ads Loaded", Toast.LENGTH_SHORT).show();
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


        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load previously selected languages
        fromLanguageCode = sharedPreferences.getInt(FROM_LANGUAGE_KEY, 0);
        toLanguageCode = sharedPreferences.getInt(TO_LANGUAGE_KEY, 0);

        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Camera Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        MicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainTranslator.class);
                startActivity(intent);
            }
        });
        sourcetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainTranslator.class);
                startActivity(intent);
            }
        });
        conversationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the selected language codes to ConversationsActivity
                Intent intent = new Intent(MainActivity2.this, ConversationsActivity.class);
                intent.putExtra("fromLanguageCode", fromLanguageCode);
                intent.putExtra("toLanguageCode", toLanguageCode);
                startActivity(intent);
            }
        });
        primiumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopUpWindow(view);
            }
        });
        historybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "History Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        //Swap Languages btn
//        swapbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                swapLanguages();
//            }
//        });

        fromspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);

                saveSelectedLanguages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);

                saveSelectedLanguages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Set up spinner adapters
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguages);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguages);
        // Set dropdown view resource
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to spinners
        fromspinner.setAdapter(fromAdapter);
        tospinner.setAdapter(toAdapter);

        // Set selected items in spinners based on saved language codes
        fromspinner.setSelection(getLanguageIndex(fromLanguageCode, fromLanguages));
        tospinner.setSelection(getLanguageIndex(toLanguageCode, toLanguages));
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
        fromspinner.setSelection(getLanguageIndex(fromLanguageCode, fromLanguages));
        tospinner.setSelection(getLanguageIndex(toLanguageCode, toLanguages));
    }
    // Method to save the selected languages to SharedPreferences
    private void saveSelectedLanguages() {
        sharedPreferences.edit().putInt(FROM_LANGUAGE_KEY, fromLanguageCode).apply();
        sharedPreferences.edit().putInt(TO_LANGUAGE_KEY, toLanguageCode).apply();
    }

    private void createPopUpWindow(View view) {
        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_premium, null);

        // Find the CardViews
        CardView weekly = popupView.findViewById(R.id.weeklySub);
        CardView monthly = popupView.findViewById(R.id.MonthlySub);
        CardView life = popupView.findViewById(R.id.LifeTimeSub);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();

        // Set layout parameters for the dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        // Set the background to null to remove default padding
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Set OnClickListener for payment options
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "Weekly clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "Monthly clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "LifeTime clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.setCancelable(true);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }


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