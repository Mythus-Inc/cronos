package util;

import org.primefaces.context.RequestContext;

public class FecharDialog {

	public static void fecharDialogCursoFechamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgCursoFechar').hide();");
	}
	
	public static void fechaDialogEquipamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgEquipamento').hide();");
	}


	public static void fecharDialogCurso() {
		RequestContext.getCurrentInstance().execute("PF('dlgCurso').hide();");
	}

	public static void fecharDialogAdiciona() {
		RequestContext.getCurrentInstance().execute("PF('dlgAdicionar').hide();");
	}

	public static void fechaDialogLocal() {
		RequestContext.getCurrentInstance().execute("PF('dlgLocal').hide();");
	}

	public static void fechaDialogSetorLocalidade() {
		RequestContext.getCurrentInstance().execute("PF('dlgSetorLocalidade').hide();");
	}

	public static void fechaDialogLocalidade() {
		RequestContext.getCurrentInstance().execute("PF('dlgLocalidade').hide();");
	}

	public static void fechaDialogVisto() {
		RequestContext.getCurrentInstance().execute("PF('dlgVisto').hide();");
	}

	public static void fechaDialogResponsavelConferencia() {
		RequestContext.getCurrentInstance().execute("PF('dlgResponsavel').hide();");
	}

	public static void fechaDialogInventario() {
		RequestContext.getCurrentInstance().execute("PF('dlgInventario').hide();");
	}

	public static void abrirDlgCorfirmarRelatFinal() {
		RequestContext.getCurrentInstance().execute("PF('dlgCorfirmarRelatFinal').show();");
	}

	public static void fecharDlgCorfirmarRelatFinal() {
		RequestContext.getCurrentInstance().execute("PF('dlgCorfirmarRelatFinal').hide();");
	}

	public static void fechaDlgVisualizar() {
		RequestContext.getCurrentInstance().execute("PF('dlgVisualizar').hide();");
	}

	public static void fechaDialogLocaisInventario() {
		RequestContext.getCurrentInstance().execute("PF('dlgLocaisInventario').hide();");
	}
 

	public static void fecharDialogAdicionaProtocolo() {
		RequestContext.getCurrentInstance().execute("PF('dlgAluno').hide();");
	}

	public static void abrirDialogFinaliza() {
		RequestContext.getCurrentInstance().execute("PF('dlgParecerFinalizar').show();");
	}

	public static void abrirDialogAbreStatus() {
		RequestContext.getCurrentInstance().execute("PF('statusDialog').show()");
	}

	public static void fecharDialogAbreStatus() {
		RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
	}

	public static void fechaDialogFinaliza() {
		RequestContext.getCurrentInstance().execute("PF('dlgParecerFinalizar').hide();");
	}

	public static void fechaDialogCorfirmar() {
		RequestContext.getCurrentInstance().execute("PF('dlgCorfirmar').hide();");
	}

	public static void fechaDialogRemove() {
		RequestContext.getCurrentInstance().execute("PF('dlgRemover').hide();");
	}
	
	public static void fechaDialogTombamenot() {
		RequestContext.getCurrentInstance().execute("PF('dlgTombamento').hide();");
	}

	public static void fechaDialogErrado() {
		RequestContext.getCurrentInstance().execute("PF('dlgRAERRAD').hide();");
	}

	public static void fechaDialogMovimentar() {
		RequestContext.getCurrentInstance().execute("PF('dlgMovimentar').hide();");
	}

	public static void fecharDialogProtocoloAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgProtocolo').hide();");
	}

	public static void fecharDialogAdicionaProtocoloServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgServidor').hide();");
	}

	public static void fecharDialogPessoaExterno() {
		RequestContext.getCurrentInstance().execute("PF('dlgExterno').hide();");
	}

	public static void fecharDialogParecer() {
		RequestContext.getCurrentInstance().execute("PF('dlgParecer').hide();");
	}

	public static void fecharDialogDarCiencia() {
		RequestContext.getCurrentInstance().execute("PF('dlgCiencia').hide();");
	}

	public static void fecharDialogDarCienciaMovimentacao() {
		RequestContext.getCurrentInstance().execute("PF('dlgMovimentacao').hide();");
	}

	public static void fecharDialogReencaminha() {
		RequestContext.getCurrentInstance().execute("PF('dlgReencaminhar').hide();");
	}

	public static void fecharDialogMovimentacaoProtocolo() {
		RequestContext.getCurrentInstance().execute("PF('dlgMovimentacao').hide();");
	}

	public static void fecharDialogComunica() {
		RequestContext.getCurrentInstance().execute("PF('dlgComunicar').hide();");
	}

	public static void fecharDialogParecerFinal() {
		RequestContext.getCurrentInstance().execute("PF('dlgParecerFinalizar').hide();");
	}

	public static void fecharDialogCpf() {
		RequestContext.getCurrentInstance().execute("PF('dlgCpf').hide();");
	}

	public static void abrirDialogPessoaExterna() {
		RequestContext.getCurrentInstance().execute("PF('dlgExterno').show();");
	}

	public static void abrirDialogLocalInventario() {
		RequestContext.getCurrentInstance().execute("PF('dlgLocaisInventario').show();");
	}

	public static void abrirDialogPessoaExternaCpf() {
		RequestContext.getCurrentInstance().execute("PF('dlgCpf').show();");
	}

	public static void fecharDialogRemove() {
		RequestContext.getCurrentInstance().execute("PF('dlgExcluir').hide();");
	}

	public static void fecharDialogTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgTurma').hide();");
	}

	public static void fecharDialogGrupo() {
		RequestContext.getCurrentInstance().execute("PF('dlgGrupo').hide();");
	}

	public static void fecharDialogAtividade() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtividade').hide();");
	}

	public static void fecharDialogVistoPresidente() {
		RequestContext.getCurrentInstance().execute("PF('dlgVisualizar').hide();");
	}

	public static void fecharDialogSetor() {
		RequestContext.getCurrentInstance().execute("PF('dlgSetor').hide();");
	}

	public static void fecharDialogRequerimento() {
		RequestContext.getCurrentInstance().execute("PF('dlgRequerimento').hide();");
	}

	public static void fecharDialogRequerimentoExcluir() {
		RequestContext.getCurrentInstance().execute("PF('dlgRemoveRequerimento').hide();");
	}

	public static void fecharDialogRequerimentoEditar() {
		RequestContext.getCurrentInstance().execute("PF('dlgEditarRequerimento').hide();");
	}

	public static void fecharDialogGrupoTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgGrupoTurma').hide();");
	}

	public static void fecharDialogAtividadeTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtividadeTurma').hide();");
	}

	public static void fecharDialogAdministrador() {
		RequestContext.getCurrentInstance().execute("PF('dlgAdministrador').hide();");
	}

	public static void fecharDialogTipoServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgTipoServidor').hide();");
	}

	public static void fecharDialogMatriz() {
		RequestContext.getCurrentInstance().execute("PF('dlgMatriz').hide();");
	}

	public static void fecharDialogServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgServidor').hide();");
	}

	public static void fecharDialogServidorCruso() {
		RequestContext.getCurrentInstance().execute("PF('dlgServidorCurso').hide();");
	}

	public static void fecharDialogServidorPermissao() {
		RequestContext.getCurrentInstance().execute("PF('dlgServidorPermissao').hide();");
	}

	public static void fecharDialogSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgSecretaria').hide();");
	}

	public static void fecharDialogProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgProfessor').hide();");
	}

	public static void fecharDialogAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgAluno').hide();");
	}

	public static void fecharDialogAlunoCurso() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoCurso').hide();");
	}

	public static void fecharDialogAlunoCursoEditar() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoCursoEditar').hide();");
	}

	public static void fecharDialogAlunoEditarCurso() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoEditarDados').hide();");
	}

	public static void fecharDialogEditarAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoEditarDados').hide();");
	}

	public static void fecharDialogDATAAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoEditarData').hide();");
	}

	public static void abrirDialogSalvarAluno() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoCurso').show();");
	}

	public static void fecharDialogCadastroPrograma() {
		RequestContext.getCurrentInstance().execute("PF('dlgPrograma').hide();");
	}

	public static void fecharDialogCadastroTiposProducao() {
		RequestContext.getCurrentInstance().execute("PF('dlgTiposProducao').hide();");
	}

	public static void fecharDialogCadastroCriterio() {
		RequestContext.getCurrentInstance().execute("PF('dlgCriterio').hide();");
	}

	public static void fecharDialogEditarSenha() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoEditarSenha').hide();");
	}

	public static void fecharDialogEditarSenhaServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgServidorEditarSenha').hide();");
	}

	public static void abrirdlgVisualizaParecer() {
		RequestContext.getCurrentInstance().execute("PF('dlgVisualizaParecer').show();");
	}

	public static void abrirDialogStatus() {
		RequestContext.getCurrentInstance().execute("PF('dlgStatus').show();");
	}

	public static void fecharDialogStatus() {
		RequestContext.getCurrentInstance().execute("PF('dlgStatus').hide();");
	}

	public static void fecharDialogAlunoEditar() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoEditar').hide();");
	}

	public static void fecharDialogAlunoCancelar() {
		RequestContext.getCurrentInstance().execute("PF('dlgCancelar').hide();");
	}

	public static void fecharDialogAlunoDestrancar() {
		RequestContext.getCurrentInstance().execute("PF('dlgAtivar').hide();");
	}

	public static void fecharDialogAlunoTrancamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgInativar').hide();");
	}

	public static void fecharDialogCertificado() {
		RequestContext.getCurrentInstance().execute("PF('dlgCertificado').hide();");
	}

	public static void fecharDialogCertificadoEdit() {
		RequestContext.getCurrentInstance().execute("PF('dlgCertificadoEditar').hide();");
	}

	public static void fecharDialogCertificadoAutenticarSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgAutenticar').hide();");
	}

	public static void fecharDialogCertificadoInvalidarSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgInvalidar').hide();");
	}

	public static void fecharDialogCertificadoPenderSecretaria() {
		RequestContext.getCurrentInstance().execute("PF('dlgPender').hide();");
	}

	public static void fecharDialogCertificadoValidarProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgValidar').hide();");
	}

	public static void fecharDialogCertificadoDetalhe() {
		RequestContext.getCurrentInstance().execute("PF('dlgDetalhes').hide();");
	}

	public static void fecharDialogCertificadoInvalidarProfessor() {
		RequestContext.getCurrentInstance().execute("PF('dlgInvalidar').hide();");
	}

	public static void fecharDialogCertificadoFileUpload() {
		RequestContext.getCurrentInstance().execute("PF('dlgUpload').hide();");
	}

	public static void fecharDialogAlunoTurma() {
		RequestContext.getCurrentInstance().execute("PF('dlgAlunoTurma').hide();");
	}

	public static void fecharDialogPendente() {
		RequestContext.getCurrentInstance().execute("PF('dlgPender').hide();");
	}
	
	public static void fecharDialogAlterarEquipamentoInventario() {
		RequestContext.getCurrentInstance().execute("PF('dlgAltConferencia').hide();");
	}
	
	public static void fecharDialogAlterarEquipamentoInventarioNaoTombado() {
		RequestContext.getCurrentInstance().execute("PF('dlgAltConferenciaNaoTombado').hide();");
	}
	
}
