package tcc.zulian.gabriel.rfidconnect.dao.itens;

import tcc.zulian.gabriel.rfidconnect.dao.itens.asynctasks.ConsultaUltimoCodigoItem;
import tcc.zulian.gabriel.rfidconnect.vo.itens.GravaItemActivity;

/**
 * Created by User on 14/08/2017.
 */
public class ItemDAO {
    public static void consultaUltimoCodigo(GravaItemActivity gravaItemActivity) {
        new ConsultaUltimoCodigoItem(gravaItemActivity).execute();
    }
}
