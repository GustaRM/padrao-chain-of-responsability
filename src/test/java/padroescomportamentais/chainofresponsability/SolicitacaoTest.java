package padroescomportamentais.chainofresponsability;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SolicitacaoTest {

    @Test
    void tiposDeProcedimentoSaoSingletons() {
        assertSame(TipoProcedimentoConsulta.getTipoProcedimentoConsulta(),
                TipoProcedimentoConsulta.getTipoProcedimentoConsulta());
        assertSame(TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia(),
                TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia());
        assertNotSame(TipoProcedimentoExame.getTipoProcedimentoExame(),
                TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia());
    }

    @Test
    void solicitacaoGuardaOTipoInformado() {
        TipoProcedimento exame = TipoProcedimentoExame.getTipoProcedimentoExame();
        assertSame(exame, new Solicitacao(exame).getTipoProcedimento());
    }

    @Test
    void trocarOTipoMudaQuemAutoriza() {
        Auditor cadeia = new AuditorAtendente(new AuditorMedico(null));
        Solicitacao solicitacao = new Solicitacao(TipoProcedimentoConsulta.getTipoProcedimentoConsulta());
        assertEquals("Atendente", cadeia.autorizar(solicitacao));

        solicitacao.setTipoProcedimento(TipoProcedimentoCirurgia.getTipoProcedimentoCirurgia());
        assertEquals("Médico Auditor", cadeia.autorizar(solicitacao));
    }

    @Test
    void tipoNuloEhRejeitado() {
        assertThrows(NullPointerException.class, () -> new Solicitacao(null));
        Solicitacao solicitacao = new Solicitacao(TipoProcedimentoExame.getTipoProcedimentoExame());
        assertThrows(NullPointerException.class, () -> solicitacao.setTipoProcedimento(null));
    }
}
