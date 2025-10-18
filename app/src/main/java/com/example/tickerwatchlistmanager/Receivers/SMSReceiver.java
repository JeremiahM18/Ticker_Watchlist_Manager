package com.example.tickerwatchlistmanager.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.tickerwatchlistmanager.MainActivity;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {

    // Optional "Ticker:" prefix, letters only (1-5), case-insensitive
    private static final Pattern TICKER_ENVELOPE =
            Pattern.compile("(?i)(?:\\bTicker\\s*:\\s*)?<<\\s*([A-Za-z]{1,5})\\s*>>");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null ||
                !Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) return;

        // Read full SMS (handles multi-part messages)
        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        StringBuilder body = new StringBuilder();
        if (msgs != null) {
            for (SmsMessage m : msgs) {
                if (m != null && m.getMessageBody() != null) {
                    body.append(m.getMessageBody());
                }
            }
        }
        String message = body.toString();

        // Try to extract the ticker
        String event;
        String ticker = null;

        // Try regex match
        if (message != null && !message.isEmpty()) {
            Matcher mat = TICKER_ENVELOPE.matcher(message);
            if (mat.find()) {
                String captured = mat.group(1);
                if (captured != null && !captured.trim().isEmpty()) {
                    event = "valid_ticker";
                    ticker = captured.trim().toUpperCase(Locale.US);
                    Toast.makeText(context, "Added " + ticker + " to watchlist.", Toast.LENGTH_SHORT).show();
                } else {
                    event = "no_watchlist";   // nothing inside << >>
                    Toast.makeText(context, "No valid watchlist entry found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                event = "no_watchlist";      // envelope not found
                Toast.makeText(context, "No valid watchlist entry found.", Toast.LENGTH_SHORT).show();
            }
        } else {
            event = "no_watchlist";
            Toast.makeText(context, "No valid watchlist entry found.", Toast.LENGTH_SHORT).show();
        }
        // Launch/bring-to-front MainActivity with result
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        launchIntent.putExtra("from_sms", true);
        launchIntent.putExtra("sms_event", event);
        if (ticker != null) {
            launchIntent.putExtra("ticker", ticker);
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        context.startActivity(launchIntent);
    }
}

