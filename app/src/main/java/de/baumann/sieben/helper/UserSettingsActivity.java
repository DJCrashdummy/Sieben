package de.baumann.sieben.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import de.baumann.sieben.R;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_settings);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.action_settings);

        getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new SettingsFragment())
                            .commit();

        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        PreferenceManager.setDefaultValues(this, R.xml.user_settings_exercises, false);
        PreferenceManager.setDefaultValues(this, R.xml.user_settings_duration, false);
    }

    @Override
    public void onBackPressed() {
        if( !getFragmentManager().popBackStackImmediate() ) super.onBackPressed();
    }

    @SuppressWarnings("deprecation")
    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences sharedPref;


        private void addChangelogListener() {
            Preference reset = findPreference("changelog");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {
                    Uri uri = Uri.parse("https://github.com/scoute-dich/Sieben/blob/master/CHANGELOG.md"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
            });
        }

        private void addHelpListener() {
            Preference reset = findPreference("help");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference pref)
                {

                    SpannableString s;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        s = new SpannableString(Html.fromHtml(getString(R.string.help_text),Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        s = new SpannableString(Html.fromHtml(getString(R.string.help_text)));
                    }

                    Linkify.addLinks(s, Linkify.WEB_URLS);

                    final AlertDialog d = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.action_help)
                            .setMessage(s)
                            .setPositiveButton(getString(R.string.about_yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                    d.show();
                    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;
                }
            });
        }

        private void addLicenseListener() {
            Preference reset = findPreference("license");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference pref)
                {

                    SpannableString s;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        s = new SpannableString(Html.fromHtml(getString(R.string.about_text),Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        s = new SpannableString(Html.fromHtml(getString(R.string.about_text)));
                    }

                    Linkify.addLinks(s, Linkify.WEB_URLS);

                    final AlertDialog d = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.about_title)
                            .setMessage(s)
                            .setPositiveButton(getString(R.string.about_yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                    d.show();
                    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;
                }
            });
        }

        private void addDonateListListener() {

            Preference reset = findPreference("donate");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {
                    Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=NP6TGYDYP9SHY"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
            });
        }

        private void add_exerciseChooseListener() {

            Preference reset = findPreference("exercises");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    Intent intent_in = new Intent(getActivity(), UserSettingsActivity_Exercises.class);
                    startActivity(intent_in);
                    getActivity().overridePendingTransition(0, 0);
                    return true;
                }
            });
        }

        private void add_exerciseDurationListener() {

            Preference reset = findPreference("exercises_duration");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    Intent intent_in = new Intent(getActivity(), UserSettingsActivity_Duration.class);
                    startActivity(intent_in);
                    getActivity().overridePendingTransition(0, 0);
                    return true;
                }
            });
        }

        private void add_durationListener() {

            Preference reset = findPreference("duration");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.seekbar_dialog_workout, null);

                    final TextView edit_title = (TextView) dialogView.findViewById(R.id.textView);
                    final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.seekBar);
                    String duration = sharedPref.getString("duration", "30");
                    edit_title.setText(duration);
                    seekBar.setProgress(Integer.parseInt(duration) - 15);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            String text = Integer.toString(progress + 15);
                            edit_title.setText(text);
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });

                    builder.setView(dialogView);
                    builder.setTitle(getString(R.string.action_duration));
                    builder.setPositiveButton(getString(R.string.app_ok), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            sharedPref.edit().putString("duration", edit_title.getText().toString()).apply();
                            final String durationWorkout = getString(R.string.app_chosenTime)
                                    + " " + sharedPref.getString("duration", "30") + " " + getString(R.string.app_sec)
                                    + " " + getString(R.string.app_standardTime) + " 30)";
                            Preference customPref = findPreference("duration");
                            customPref.setSummary(durationWorkout);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.app_no), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    return true;
                }
            });
        }

        private void add_duration_break_Listener() {

            Preference reset = findPreference("duration2");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.seekbar_dialog_break, null);

                    final TextView edit_title = (TextView) dialogView.findViewById(R.id.textView);
                    final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.seekBar);
                    String duration = sharedPref.getString("duration2", "10");
                    edit_title.setText(duration);
                    seekBar.setProgress(Integer.parseInt(duration) - 10);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            String text = Integer.toString(progress + 10);
                            edit_title.setText(text);
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {}
                        public void onStopTrackingTouch(SeekBar seekBar) {}
                    });

                    builder.setView(dialogView);
                    builder.setTitle(getString(R.string.action_duration2));
                    builder.setPositiveButton(getString(R.string.app_ok), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            sharedPref.edit().putString("duration2", edit_title.getText().toString()).apply();
                            final String durationBreak = getString(R.string.app_chosenTime)
                                    + " " + sharedPref.getString("duration2", "10") + " " + getString(R.string.app_sec)
                                    + " " + getString(R.string.app_standardTime) + " 10)";
                            Preference customPref = findPreference("duration2");
                            customPref.setSummary(durationBreak);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.app_no), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    return true;
                }
            });
        }

        private void add_statListener() {

            Preference reset = findPreference("stat");

            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    Intent intent_in = new Intent(getActivity(), Activity_statistics.class);
                    startActivity(intent_in);
                    getActivity().overridePendingTransition(0, 0);
                    return true;
                }
            });
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings_exercises, false);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings_duration, false);
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String durationWorkout = getString(R.string.app_chosenTime)
                    + " " + sharedPref.getString("duration", "30") + " " + getString(R.string.app_sec)
                    + " " + getString(R.string.app_standardTime) + " 30)";
            final String durationBreak = getString(R.string.app_chosenTime)
                    + " " + sharedPref.getString("duration2", "10") + " " + getString(R.string.app_sec)
                    + " " + getString(R.string.app_standardTime) + " 10)";

            addPreferencesFromResource(R.xml.user_settings);
            addHelpListener();
            addLicenseListener();
            addChangelogListener();
            addDonateListListener();
            add_exerciseChooseListener();
            add_durationListener();
            add_duration_break_Listener();
            add_exerciseDurationListener();
            add_statListener();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Preference customPref = findPreference("duration");
                    customPref.setSummary(durationWorkout);
                    Preference customPref2 = findPreference("duration2");
                    customPref2.setSummary(durationBreak);
                }
            }, 200);


        }
    }
}
