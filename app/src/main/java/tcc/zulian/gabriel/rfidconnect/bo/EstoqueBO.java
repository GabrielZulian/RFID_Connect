package tcc.zulian.gabriel.rfidconnect.bo;

import org.joda.time.DateTime;

/**
 * Created by User on 14/08/2017.
 */
public class EstoqueBO {
    Integer codigo, quantidade;
    DateTime dataUltimaAtualizacao;

    public EstoqueBO() {
        this.codigo = 1;
        this.quantidade = 0;
        this.dataUltimaAtualizacao = new DateTime();
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public DateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(DateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
