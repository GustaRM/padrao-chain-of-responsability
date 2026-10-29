package padroescomportamentais.chainofresponsability;

public class TipoProcedimentoInternacao implements TipoProcedimento {

    private static TipoProcedimentoInternacao tipoProcedimentoInternacao = new TipoProcedimentoInternacao();

    private TipoProcedimentoInternacao() {};

    public static TipoProcedimentoInternacao getTipoProcedimentoInternacao() {
        return tipoProcedimentoInternacao;
    }
}
