package weatherTeam.com.forecast;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import weatherTeam.com.stormy.R;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            Context context = getActivity();
            savedInstanceState = getArguments();
            String ErrorTitle = savedInstanceState.getString(MainActivity.ERROR_KEY);
            android.support.v7.app.AlertDialog.Builder builder ;

            if (ErrorTitle.contains("Error")) {
                builder = new android.support.v7.app.AlertDialog.Builder(context)
                        .setTitle(ErrorTitle)
                        .setMessage(R.string.error_message)
                        .setPositiveButton(R.string.error_ok_button, null);
            } else {
                builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setMessage(R.string.ErrorGpsOff)
                        .setCancelable(false)
                        .setPositiveButton(R.string.EnableGps,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent callGPSSettingIntent = new Intent(android.provider
                                                .Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(callGPSSettingIntent);
                                    }
                                });
                builder.setNegativeButton(R.string.Cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
            }
            return builder.create();
    }
}
