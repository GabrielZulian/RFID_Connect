package tcc.zulian.gabriel.rfidconnect.bo;

/**
 * Created by User on 14/08/2017.
 */
public class ItemBO {
    Integer codigo;
    String descricao, unidade, numeroSerial;
    Double peso;
    EstoqueBO estoqueBO;

    public ItemBO() {
        this.codigo = 1;
        this.descricao = "";
        this.unidade = "UN";
        this.numeroSerial = "0000000000000";
        this.peso = 0.00;
        this.estoqueBO = new EstoqueBO();
    }

    public ItemBO(String tagMsg) {
        ItemBO itemBO = new ItemBO();

       String[] messageParts = tagMsg.split(";");


    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getNumeroSerial() {
        return numeroSerial;
    }

    public void setNumeroSerial(String numeroSerial) {
        this.numeroSerial = numeroSerial;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public EstoqueBO getEstoqueBO() {
        return estoqueBO;
    }

    public void setEstoqueBO(EstoqueBO estoqueBO) {
        this.estoqueBO = estoqueBO;
    }

    public void createFromTagMsg(String msg) {

    }
}
