package padroescomportamentais.chainofresponsability;

public class TipoProcedimentoExame implements TipoProcedimento {

    private static TipoProcedimentoExame tipoProcedimentoExame = new TipoProcedimentoExame();

    private TipoProcedimentoExame() {};

    public static TipoProcedimentoExame getTipoProcedimentoExame() {
        return tipoProcedimentoExame;
    }
}
