package padroescomportamentais.chainofresponsability;

public class Main {

    public static void main(String[] args) {
        // monta a cadeia: Atendente -> Enfermeiro -> Médico -> Diretor Clínico
        Auditor diretor = new AuditorDiretorClinico(null);
        Auditor medico = new AuditorMedico(diretor);
        Auditor enfermeiro = new AuditorEnfermeiro(medico);
        Auditor atendente = new AuditorAtendente(enfermeiro);

        TipoProcedimento[] tipos = {
                TipoProcedimentoConsulta.getTipoProcedimentoConsulta(),
                TipoProcedimentoExame.getTipoProcedimentoExame(),
                TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia(),
                TipoProcedimentoInternacao.getTipoProcedimentoInternacao(),
                TipoProcedimentoExperimental.getTipoProcedimentoExperimental()
        };

        for (TipoProcedimento tipo : tipos) {
            Solicitacao solicitacao = new Solicitacao(tipo);
            System.out.println(tipo.getClass().getSimpleName() + " -> " + atendente.autorizar(solicitacao));
        }
    }
}
