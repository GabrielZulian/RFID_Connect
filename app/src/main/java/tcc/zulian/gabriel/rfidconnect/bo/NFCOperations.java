package tcc.zulian.gabriel.rfidconnect.bo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotEnabled;
import tcc.zulian.gabriel.rfidconnect.bo.exceptions.NFCNotSupported;

/**
 * Created by User on 13/07/2017.
 */
public class NFCOperations {

    private Activity activity;
    private NfcAdapter nfcAdpt;

    public NFCOperations(Activity activity) {
        this.activity = activity;
    }

    public void verifyNFC() throws NFCNotSupported, NFCNotEnabled {

        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

        if (nfcAdpt == null)
            throw new NFCNotSupported();

        if (!nfcAdpt.isEnabled())
            throw new NFCNotEnabled();
    }

    public void enableDispatch() {
        Intent nfcIntent = new Intent(activity, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, nfcIntent, 0);
        IntentFilter[] intentFiltersArray = new IntentFilter[]{};
        String[][] techList = new String[][]{{Ndef.class.getName()}, {NdefFormatable.class.getName()}};

        nfcAdpt.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techList);
    }

    public void disableDispatch() {
        nfcAdpt.disableForegroundDispatch(activity);
    }

    public String readTag(Tag tag) {

        Ndef ndef = Ndef.get(tag);
        String msg = null;

        try {
            ndef.connect();

            NdefMessage mensagem = ndef.getNdefMessage();

            NdefRecord[] records = mensagem.getRecords();

            msg = new String(records[0].getPayload());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return msg;
    }

    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null) {
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                } else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NdefMessage createUriMessage(String content, String type) {
        NdefRecord record = NdefRecord.createUri(type + content);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
        return msg;
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}