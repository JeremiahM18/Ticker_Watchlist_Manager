package com.example.tickerwatchlistmanager.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.tickerwatchlistmanager.MainActivity;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) return;

        // Rea message body safely (handles multi-part messages)
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

        // Parse Ticker:<<...>>
        ParseResult pr = parseTickerEnvelope(message);
        String event;
        String ticker = null;

        if (!pr.foundEnvelope) {
            event = "no_watchlist";
            Toast.makeText(context, "No valid watchlist entry found.", Toast.LENGTH_SHORT).show();
        } else {
            String norm = normalize(pr.rawInside);
            if (isLettersOnly(norm) && norm.length() >= 1 && norm.length() <= 5) {
                event = "valid_ticker";
                ticker = norm;
                Toast.makeText(context, "Adding " + ticker + " to watchlist...", Toast.LENGTH_SHORT).show();
            } else {
                event = "invalid_ticker";
                Toast.makeText(context, "Invalid ticker: " + norm, Toast.LENGTH_SHORT).show();
            }
        }

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

    private static ParseResult parseTickerEnvelope(String body) {
        ParseResult r = new ParseResult();
        if (body == null) return r;
        int start = indexOfIgnoreCase(body, "Ticker:<<");
        if (start < 0) return r;
        int openEnd = start + "Ticker:<<".length();
        int close = body.indexOf(">>", openEnd);
        if (close < 0) return r;
        r.foundEnvelope = true;
        r.rawInside = body.substring(openEnd, close).trim();
        return r;
    }

    private static int indexOfIgnoreCase(String hay, String needle) {
        int hLen = hay.length();
        int nLen = needle.length();
        for (int i = 0; i <= hLen - nLen; i++) {
            if (hay.regionMatches(true, i, needle, 0, nLen)) {
                return i;
            }
        }
        return -1;
    }

    private static String normalize(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }

    private static boolean isLettersOnly(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 'A' || c > 'Z') return false;
        }
        return true;
    }

        private static class ParseResult {
            boolean foundEnvelope;
            String rawInside;
        }
    }

