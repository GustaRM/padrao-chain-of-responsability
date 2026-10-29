package padroescomportamentais.chainofresponsability;

public class TipoProcedimentoConsulta implements TipoProcedimento {

    private static TipoProcedimentoConsulta tipoProcedimentoConsulta = new TipoProcedimentoConsulta();

    private TipoProcedimentoConsulta() {};

    public static TipoProcedimentoConsulta getTipoProcedimentoConsulta() {
        return tipoProcedimentoConsulta;
    }
}
