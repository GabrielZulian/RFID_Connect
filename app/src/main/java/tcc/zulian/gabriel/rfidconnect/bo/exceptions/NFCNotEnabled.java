package tcc.zulian.gabriel.rfidconnect.bo.exceptions;

/**
 * Created by User on 25/09/2017.
 */
public class NFCNotEnabled extends Exception {
    @Override
    public String toString() {
        return "NFC não está ativo";
    }
}
