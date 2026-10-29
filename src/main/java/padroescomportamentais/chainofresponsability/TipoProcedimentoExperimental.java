package padroescomportamentais.chainofresponsability;

public class TipoProcedimentoExperimental implements TipoProcedimento {

    private static TipoProcedimentoExperimental tipoProcedimentoExperimental = new TipoProcedimentoExperimental();

    private TipoProcedimentoExperimental() {};

    public static TipoProcedimentoExperimental getTipoProcedimentoExperimental() {
        return tipoProcedimentoExperimental;
    }
}
