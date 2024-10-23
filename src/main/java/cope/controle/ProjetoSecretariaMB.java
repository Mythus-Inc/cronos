package cope.controle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Part;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Pessoa;
import base.modelo.Servidor;

import cope.modelo.ArquivoProjeto;
import cope.modelo.Avaliacao;
import cope.modelo.Participante;
import cope.modelo.Projeto; 
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
import cope.service.AvaliacaoService;
import cope.service.ParticipanteService;
import cope.service.ProjetoService;
import dao.GenericDAO;
import util.CaminhoArquivos;
import util.EnviarEmail;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;

@ViewScoped
@Named("projetoSecretariaMB")
public class ProjetoSecretariaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private CaminhoArquivos caminhoArquivo;
	private List<Projeto> listaProjeto;
	private Projeto projetoSelecionado;
	private Avaliacao avaliacaoProjetoSelecionado;
	private ArquivoProjeto arquivoProjetoSelecionado;
	private List<ArquivoProjeto> listaarquivoProjetoSelecionado;
	private String descricao;
	private boolean desativarNumProcesso;

	@Inject
	private GenericDAO<Projeto> daoProjeto;

	@Inject
	private GenericDAO<Avaliacao> daoAvaliacao;

	@Inject
	private GenericDAO<ArquivoProjeto> daoArquivoProjeto;
	@Inject
	private GenericDAO<Participante> daoParticipantes;

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private ArquivoProjetoService arquivoProjetoService;

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private ParticipanteService participanteService;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void inicializar() {
		listaarquivoProjetoSelecionado = new ArrayList<>();
		avaliacaoProjetoSelecionado = new Avaliacao();
		buscaProjeto();
	}

	public void buscaProjeto() {
		listaProjeto = daoProjeto.listarCodicaoLivre(Projeto.class, "situacao = '"
				+ StatusSubmissao.AGUARDANDO_AVALIACAO + "' OR situacao = '" + StatusSubmissao.EM_AVALIACAO + "'");

		List<ArquivoProjeto> avaliacaoPendente = new ArrayList<>();
		avaliacaoPendente = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				" status is true and situacao = 'AGUARDANDO_VISTO' and avaliacao.autenticacaoPresidente is false ");

		for (ArquivoProjeto a : avaliacaoPendente) {
			if (verificaAddLista(a))
				listaProjeto.add(a.getProjeto());
		}
	}

	public boolean verificaAddLista(ArquivoProjeto arquivo) {
		for (Projeto p : listaProjeto) {
			if (p.getId() == arquivo.getProjeto().getId()) {
				return false;
			}
		}
		return true;
	}

	public void selecionaProjeto(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
		arquivoProjetoSelecionado = (ArquivoProjeto) daoArquivoProjeto
				.listarCodicaoLivre(ArquivoProjeto.class, "situacao = '" + StatusSubmissao.AGUARDANDO_AVALIACAO
						+ "' AND projeto.id = " + projetoSelecionado.getId())
				.iterator().next();
		arquivoProjetoSelecionado.setAvaliacao(new Avaliacao());
	}

	public void selecionaProjetoEncaminhar(ArquivoProjeto projetoSelecionado) {

		this.projetoSelecionado = projetoSelecionado.getProjeto();

		System.out.println("projeto selecionado " + projetoSelecionado.getAvaliacao());

		if (projetoSelecionado.getAvaliacao() != null) {
			arquivoProjetoSelecionado = projetoSelecionado;
		} else {
			arquivoProjetoSelecionado = projetoSelecionado;
			arquivoProjetoSelecionado.setAvaliacao(new Avaliacao());
		}

	}

	public void buscarParecer(String avaliacao) {
		descricao = avaliacao;

	}

	public void visualizarProjeto(Projeto projetoSelecionado) {

		this.projetoSelecionado = projetoSelecionado;
		this.projetoSelecionado.setDataAprovacao(new Date());
		if ((projetoSelecionado.isProjetoAntigo()
				|| projetoSelecionado.isCiencia()) && projetoSelecionado.getNumeroProcesso() == null)
			desativarNumProcesso = false;
		else
			desativarNumProcesso = true;
		System.out.println("num "+projetoSelecionado.getNumeroProcesso());
		System.out.println(desativarNumProcesso);
		
		buscarArquivosProjeto(projetoSelecionado);
	}

	public void buscarArquivosProjeto(Projeto projetoSelecionado) {
		listaarquivoProjetoSelecionado = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				"status is true and projeto.id = " + projetoSelecionado.getId() + " order by id desc");
	}

	public void buscarAvaliacoes(ArquivoProjeto arquivo) {

		avaliacaoProjetoSelecionado = new Avaliacao();

		System.out.println("avalia��o " + arquivo.getAvaliacao());

		if (arquivo.getAvaliacao() != null) {

			List<Avaliacao> list = daoAvaliacao.listarCodicaoLivre(Avaliacao.class,
					" id = '" + arquivo.getAvaliacao().getId() + "'");
			if (list.size() > 0)
				avaliacaoProjetoSelecionado = list.get(0);
		}

		arquivoProjetoSelecionado = arquivo;

		// arquivoProjetoSelecionado = (ArquivoProjeto)
		// daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
		// "projeto.id = " + arquivo.getProjeto().getId() + "ORDER BY versao
		// ASC").iterator().next();
	}

	public StreamedContent downloadArquivoProjeto(ArquivoProjeto arquivo) throws FileNotFoundException {
		String path = caminhoArquivo.caminhoPDFCope() + "/" + arquivo.getProjeto().getId() + "/" + arquivo.getId()
				+ ".pdf";
		FileInputStream stream = new FileInputStream(path);
		return new DefaultStreamedContent(stream, "application/pdf", "projeto.pdf");
	}

	public void verificaRelatorioFinal() {

		if (arquivoProjetoSelecionado.isRelatorioFinal()) {
			if (avaliacaoProjetoSelecionado.getParecer1().toString().equals("ACEITO COM RESSALVAS")
					|| avaliacaoProjetoSelecionado.getParecer2() != null
							&& avaliacaoProjetoSelecionado.getParecer2().toString().equals("ACEITO COM RESSALVAS")) {
				vistoPresidente();
			} else {
				FecharDialog.abrirDlgCorfirmarRelatFinal();
			}
		} else {
			vistoPresidente();
		}
	}

	public void finalizaRelatorio() {

		avaliacaoProjetoSelecionado.setPresidente((Servidor) usuarioSessao.recuperarServidor());
		avaliacaoProjetoSelecionado.setAutenticacaoPresidente(true);
		avaliacaoService.inserirAlterar(avaliacaoProjetoSelecionado);

		arquivoProjetoSelecionado.setSituacao(StatusSubmissao.APROVADO);
		arquivoProjetoService.inserirAlterar(arquivoProjetoSelecionado);

		if (projetoSelecionado.getSituacao().equals(StatusSubmissao.EM_AVALIACAO)) {
			projetoSelecionado.setSituacao(StatusSubmissao.FINALIZADO);
			projetoSelecionado.setSituacaoProjeto(StatusSubmissao.FINALIZADO);
			projetoService.inserirAlterar(projetoSelecionado);

			List<Participante> lisParticipante = new ArrayList<>();
			lisParticipante = daoParticipantes.listar(Participante.class,
					" status is true and projeto = '" + projetoSelecionado.getId() + "'");

			for (Participante participante : lisParticipante) {
				participante.setDataFimAtividade(new Date());
				participanteService.inserirAlterar(participante);
			}
		}

		FecharDialog.fecharDlgCorfirmarRelatFinal();
		FecharDialog.fechaDialogVisto();
		FecharDialog.fechaDlgVisualizar();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscaProjeto();

	}

	public void vistoPresidente() {

		avaliacaoProjetoSelecionado.setPresidente((Servidor) usuarioSessao.recuperarServidor());
		avaliacaoProjetoSelecionado.setAutenticacaoPresidente(true);
		avaliacaoService.inserirAlterar(avaliacaoProjetoSelecionado);

		if (avaliacaoProjetoSelecionado.getParecer2() != null) {
			if (avaliacaoProjetoSelecionado.getParecer1().toString().equals("ACEITO")
					&& avaliacaoProjetoSelecionado.getParecer2().toString().equals("ACEITO")) {
				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.APROVADO);

				alterarAvaliacaoProjetoAceito();
				 
			 

			} else if (avaliacaoProjetoSelecionado.getParecer1().toString().equals("ACEITO COM RESSALVAS")
					&& avaliacaoProjetoSelecionado.getParecer2().toString().equals("ACEITO COM RESSALVAS")) {
				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.APROVADORESSALVAS);

				alterarAvaliacaoProjetoAceitoRessalvas();

			} // aqui vem mais uma condi��o, caso um aceita e o outro n�o, o q
				// fazer ?

		} else {
			if (avaliacaoProjetoSelecionado.getParecer1().toString().equals("ACEITO")) {
				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.APROVADO);

				alterarAvaliacaoProjetoAceito();

			} else {
				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.APROVADORESSALVAS);

				alterarAvaliacaoProjetoAceitoRessalvas();

			}

		}

		arquivoProjetoService.inserirAlterar(arquivoProjetoSelecionado);
		alteraDataAprovacao(projetoSelecionado);

		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fechaDialogVisto();
		buscaProjeto();

	}

	public void alterarAvaliacaoProjetoAceitoRessalvas() {

		if (projetoSelecionado.getSituacao().equals(StatusSubmissao.EM_AVALIACAO)) {
			projetoSelecionado.setSituacao(StatusSubmissao.APROVADORESSALVAS);
			if (arquivoProjetoSelecionado.getTipo().equals(TipoArquivoProjeto.ARQUIVO_PROJETO))
				projetoSelecionado.setSituacaoProjeto(StatusSubmissao.APROVADORESSALVAS);
			projetoService.inserirAlterar(projetoSelecionado);

		}
	}

	public void alterarAvaliacaoProjetoAceito() {

		if (projetoSelecionado.getSituacao().equals(StatusSubmissao.EM_AVALIACAO)) {
			projetoSelecionado.setSituacao(StatusSubmissao.APROVADO);
			if (arquivoProjetoSelecionado.getTipo().equals(TipoArquivoProjeto.ARQUIVO_PROJETO)) {
				projetoSelecionado.setSituacaoProjeto(StatusSubmissao.APROVADO);
				alterarInicioAtividades(projetoSelecionado);
				if(projetoSelecionado.getDataAprovacao().after(projetoSelecionado.getDataInicio())){
					projetoSelecionado.setDataInicio(projetoSelecionado.getDataAprovacao());
				}
			}
		
			
			projetoService.inserirAlterar(projetoSelecionado);

		}
	}

	public void alteraDataAprovacao(Projeto projeto) {

		if (projetoSelecionado.getDataTermino() != null) {
			
			 
			  
			// aqui eu peguei a data q eu quero fazer o calculo.
			LocalDate dataAprovado = projetoSelecionado.getDataAprovacao().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			// aqui peguei a outra data q tamb�m quero calcular
			LocalDate dataFinalizacao = projetoSelecionado.getDataTermino().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();

			// aqui eu disse q vai somar 6 meses em cima da minha dataAprovado
			LocalDate meses = dataAprovado.plusMonths(6);
			// aqui eu converto ela para o formato date novamente e depois s�
			// mando salvar no banco
			Date date = java.sql.Date.valueOf(meses);

			if (meses.isBefore(dataFinalizacao)) {
				projeto.setProximoRelatorio(date);
			} else {
				projeto.setProximoRelatorio(projetoSelecionado.getDataTermino());
			}
		} else {

			LocalDate dataAprovado = projetoSelecionado.getDataAprovacao().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate meses = dataAprovado.plusMonths(6);
			Date date = java.sql.Date.valueOf(meses);
			projeto.setProximoRelatorio(date);

		}
		projetoService.inserirAlterar(projeto);
		FecharDialog.fecharDialogVistoPresidente();

	}

	public void alterarInicioAtividades(Projeto projeto) {
		List<Participante> lisParticipante = new ArrayList<>();
		lisParticipante = daoParticipantes.listar(Participante.class,
				" status is true and projeto = '" + projeto.getId() + "'");

		for (Participante participante : lisParticipante) {
			participante.setDataInicioAtividade(projeto.getDataAprovacao());
			participanteService.inserirAlterar(participante);
		}
	}

	public void cobrarAvaliacao(Pessoa avaliador) {
		// TODO: melhorar mensagem
		EnviarEmail.enviarEmail(avaliador.getUsuario(), "Você está em divida com o COPE",
				"existem projetos que aguardam sua avaliação");
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
	}

	public void notificar() {

		System.out.println("aqui no m�todo");

		if (projetoSelecionado.getNumeroProcesso().equals("")) {
			ExibirMensagem.exibirMensagem(Mensagem.NUMEROPROCESSO);
		} else {

			if (validaNumeroPorcesso() && validaNumeroPorcessoIgual()) {
				ExibirMensagem.exibirMensagem(Mensagem.NUMPROCESSO);
				projetoSelecionado.setNumeroProcesso(null);
			} else {
				System.out.println("entro no else");
				projetoService.inserirAlterar(projetoSelecionado);

				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				RequestContext.getCurrentInstance().update(":pnDialog");
				RequestContext.getCurrentInstance().execute("PF('dlgVisualizar').hide();");

				buscaProjeto();
				System.out.println("entro no if");

				RequestContext.getCurrentInstance().update(":pnDialog");
				RequestContext.getCurrentInstance().execute("PF('dlgVisualizar').hide();");
			}
		}
		/*
		 * if
		 * (arquivoProjetoSelecionado.getAvaliacao().isAutenticacaoPresidente())
		 * { ExibirMensagem.exibirMensagem("mensagem.sucesso");
		 * RequestContext.getCurrentInstance().update(":pnDialog");
		 * RequestContext.getCurrentInstance().execute(
		 * "PF('dlgVisualizar').hide();"); } else {
		 * ExibirMensagem.exibirMensagem(
		 * "mensagem.erro.autenticar.presidente.cope"); }
		 */
	}

	public boolean validaNumeroPorcesso() {

		try {

			List<Projeto> listProjetoVerificaNumProcesso = new ArrayList<>();
			listProjetoVerificaNumProcesso = daoProjeto.listarCodicaoLivre(Projeto.class,
					" numeroProcesso ='" + projetoSelecionado.getNumeroProcesso() + "'");
			System.out.println("tamanho lista 1 "+listProjetoVerificaNumProcesso.size());
			if (listProjetoVerificaNumProcesso.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no verificar numeros processos iguais");
			e.printStackTrace();
		}
		return true;
	}

	public boolean validaNumeroPorcessoIgual() {

		try {

			List<Projeto> listProjetoVerificaNumProcesso = new ArrayList<>();
			listProjetoVerificaNumProcesso = daoProjeto.listarCodicaoLivre(Projeto.class, " numeroProcesso ='"
					+ projetoSelecionado.getNumeroProcesso() + "' and id = '" + projetoSelecionado.getId() + "'");

			System.out.println("tamanho lista 2 " + listProjetoVerificaNumProcesso.size());

			for (Projeto p : listProjetoVerificaNumProcesso) {
				System.out.println("num pro encontrado " + p.getId());
			}

			System.out.println("id proj passado " + projetoSelecionado.getId());

			if (listProjetoVerificaNumProcesso.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public void encaminharProjeto() {
		// TODO: melhorar as mensagem

		System.out.println(validaNumeroPorcesso());
		System.out.println(validaNumeroPorcessoIgual());
		//quando encontra ele returna true 
		//se n�o encontra e�e retorna true 
		System.out.println("numero pass "+projetoSelecionado.getNumeroProcesso());

		if (validaNumeroPorcesso() && validaNumeroPorcessoIgual()) {
			ExibirMensagem.exibirMensagem(Mensagem.NUMPROCESSO);
			projetoSelecionado.setNumeroProcesso(null);
		} else {

			if (arquivoProjetoSelecionado.getAvaliacao().getAvaliador1() != null
					&& arquivoProjetoSelecionado.getAvaliacao().getAvaliador2() != null) {

				EnviarEmail.enviarEmail(arquivoProjetoSelecionado.getAvaliacao().getAvaliador1().getUsuario(),
						"Nova avaliação pendente", "uma nova avaliação requer sua atenção.");
				EnviarEmail.enviarEmail(arquivoProjetoSelecionado.getAvaliacao().getAvaliador2().getUsuario(),
						"Nova avaliação pendente", "uma nova avaliação requer sua atenção.");

				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				arquivoProjetoSelecionado.getAvaliacao().setParecer1("PENDENTE");
				arquivoProjetoSelecionado.getAvaliacao().setParecer2("PENDENTE");
				if (projetoSelecionado.getSituacao().equals(StatusSubmissao.AGUARDANDO_AVALIACAO))
					projetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				if (arquivoProjetoSelecionado.getAvaliacao().getId() == null)
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				else
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				arquivoProjetoService.inserirAlterar(arquivoProjetoSelecionado);
				projetoService.inserirAlterar(projetoSelecionado);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				RequestContext.getCurrentInstance().execute("PF('dlgProjeto').hide();");

			} else if (arquivoProjetoSelecionado.getAvaliacao().getAvaliador1() == null) {

				EnviarEmail.enviarEmail(arquivoProjetoSelecionado.getAvaliacao().getAvaliador2().getUsuario(),
						"Nova avaliação pendente", "uma nova avaliação requer sua atenção.");

				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				arquivoProjetoSelecionado.getAvaliacao().setParecer2("PENDENTE");

				if (projetoSelecionado.getSituacao().equals(StatusSubmissao.AGUARDANDO_AVALIACAO))
					projetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				if (arquivoProjetoSelecionado.getAvaliacao().getId() == null)
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				else
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				arquivoProjetoService.inserirAlterar(arquivoProjetoSelecionado);
				projetoService.inserirAlterar(projetoSelecionado);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				RequestContext.getCurrentInstance().execute("PF('dlgProjeto').hide();");

			} else {

				EnviarEmail.enviarEmail(arquivoProjetoSelecionado.getAvaliacao().getAvaliador1().getUsuario(),
						"Nova avaliação pendente", "uma nova avaliação requer sua atenção.");

				arquivoProjetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				arquivoProjetoSelecionado.getAvaliacao().setParecer1("PENDENTE");

				if (projetoSelecionado.getSituacao().equals(StatusSubmissao.AGUARDANDO_AVALIACAO))
					projetoSelecionado.setSituacao(StatusSubmissao.EM_AVALIACAO);
				if (arquivoProjetoSelecionado.getAvaliacao().getId() == null)
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				else
					avaliacaoService.inserirAlterar(arquivoProjetoSelecionado.getAvaliacao());
				arquivoProjetoService.inserirAlterar(arquivoProjetoSelecionado);
				projetoService.inserirAlterar(projetoSelecionado);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				RequestContext.getCurrentInstance().execute("PF('dlgProjeto').hide();");
			}

			buscaProjeto();
			RequestContext.getCurrentInstance().update(":pnDialog");
			RequestContext.getCurrentInstance().update(":pnLista");
			FecharDialog.fechaDlgVisualizar();

			buscaProjeto();
			RequestContext.getCurrentInstance().update(":pnLista");
			System.out.println("entro aqui");
			buscaProjeto();
			RequestContext.getCurrentInstance().update(":pnLista");

		}

	}

	public void removerArquivoProjeto(ArquivoProjeto arquivo) {

		arquivo.setStatus(false);
		arquivoProjetoService.inserirAlterar(arquivo);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscarArquivosProjeto(arquivo.getProjeto());

		for (ArquivoProjeto projeto : listaarquivoProjetoSelecionado) {
			System.out.println("ids projeto encontrado " + projeto.getId());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Servidor> completaServidor(String query) {
		return daoServidor.listarCodicaoLivre(Servidor.class, "nome like '" + query + "%'");
	}

	// GET SET

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public ArquivoProjeto getArquivoProjetoSelecionado() {
		return arquivoProjetoSelecionado;
	}

	public void setArquivoProjetoSelecionado(ArquivoProjeto arquivoProjetoSelecionado) {
		this.arquivoProjetoSelecionado = arquivoProjetoSelecionado;
	}

	public List<ArquivoProjeto> getListaarquivoProjetoSelecionado() {
		return listaarquivoProjetoSelecionado;
	}

	public void setListaarquivoProjetoSelecionado(List<ArquivoProjeto> listaarquivoProjetoSelecionado) {
		this.listaarquivoProjetoSelecionado = listaarquivoProjetoSelecionado;
	}

	public Avaliacao getAvaliacaoProjetoSelecionado() {
		return avaliacaoProjetoSelecionado;
	}

	public void setAvaliacaoProjetoSelecionado(Avaliacao avaliacaoProjetoSelecionado) {
		this.avaliacaoProjetoSelecionado = avaliacaoProjetoSelecionado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isDesativarNumProcesso() {
		return desativarNumProcesso;
	}

	public void setDesativarNumProcesso(boolean desativarNumProcesso) {
		this.desativarNumProcesso = desativarNumProcesso;
	}

}
