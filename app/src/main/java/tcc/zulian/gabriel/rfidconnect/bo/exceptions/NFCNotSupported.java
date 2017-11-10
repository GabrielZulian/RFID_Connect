package tcc.zulian.gabriel.rfidconnect.bo.exceptions;

/**
 * Created by User on 25/09/2017.
 */
public class NFCNotSupported extends Exception {
    @Override
    public String toString() {
        return "Seu aparelho não suporta NFC";
    }
}
