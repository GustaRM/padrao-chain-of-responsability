package padroescomportamentais.chainofresponsability;

public class TipoProcedimentoCirurgia implements TipoProcedimento {

    private static TipoProcedimentoCirurgia tipoProcedimentoCirurgia = new TipoProcedimentoCirurgia();

    private TipoProcedimentoCirurgia() {};

    public static TipoProcedimentoCirurgia getTipoProcedimentoCirurgia() {
        return tipoProcedimentoCirurgia;
    }
}
