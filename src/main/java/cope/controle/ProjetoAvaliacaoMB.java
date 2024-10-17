package cope.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Permissao;
import ac.modelo.Pessoa;

import cope.modelo.ArquivoProjeto;
import cope.modelo.Avaliacao;
import cope.modelo.Criterios;
import cope.modelo.Participante;
import cope.modelo.enums.StatusSubmissao;
import cope.service.ArquivoProjetoService;
import cope.service.AvaliacaoService;
import cope.service.ParticipanteService;
import dao.GenericDAO;
import util.Mensagem;

@ViewScoped
@Named("projetoAvaliacaoMB")
public class ProjetoAvaliacaoMB {

	private boolean isAvalicao;
	private Pessoa pessoaLogada;
	private List<ArquivoProjeto> listaArquivoProjetoPendente;
	private List<Criterios> listaCriterios;

	private ArquivoProjeto arquivoProjeto;
	private Avaliacao avaliacao;
	private List<Participante> listaParticipanteProjeto;
	private List<ArquivoProjeto> listaArquivo;

	@Inject
	private GenericDAO<ArquivoProjeto> daoArquivoProjeto;

	@Inject
	private GenericDAO<Participante> daoParticipante;

	@Inject
	private GenericDAO<Criterios> daoCriterios;

	@Inject
	private GenericDAO<Permissao> daoPermissao;

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private ArquivoProjetoService arquivoProjetoService;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void inicializar() {
		pessoaLogada = new Pessoa();
		pessoaLogada = usuarioSessao.recuperarPessoa();
		buscarProjetosPendentes();
		isAvalicao = false;
		listaCriterios = new ArrayList<>();
		avaliacao = new Avaliacao();

	}

	public void buscarProjetosPendentes() {
		String condicao = "situacao = '" + StatusSubmissao.EM_AVALIACAO + "' AND status is true AND (avaliacao.avaliador1.id = "
				+ pessoaLogada.getId() + " OR avaliacao.avaliador2.id = " + pessoaLogada.getId() + ")";
		listaArquivoProjetoPendente = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class, condicao);
	}

	@SuppressWarnings("unchecked")
	public void selecionaProjeto(ArquivoProjeto arquivoProjeto) {
		this.arquivoProjeto = arquivoProjeto;
		isAvalicao = true;
		listaParticipanteProjeto = daoParticipante.listarCodicaoLivre(Participante.class,
				" status is true and projeto.id =" + this.arquivoProjeto.getProjeto().getId());
		listaArquivo = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				"status is true and projeto.id = " + this.arquivoProjeto.getProjeto().getId() + " order by id desc");
	}

	public void buscarCriterios() {
		listaCriterios = daoCriterios.listaComStatus(Criterios.class);
	}

	@SuppressWarnings("unchecked")
	public void salvarParecer() {

		if (pessoaLogada.getId() == arquivoProjeto.getAvaliacao().getAvaliador1().getId()) {
			arquivoProjeto.getAvaliacao().setObservacoes1(avaliacao.getObservacoes1());
			arquivoProjeto.getAvaliacao().setParecer1(avaliacao.getParecer1());
			arquivoProjeto.getAvaliacao().setDataParecer1(new Date());
		} else {
			arquivoProjeto.getAvaliacao().setObservacoes2(avaliacao.getObservacoes1());
			arquivoProjeto.getAvaliacao().setParecer2(avaliacao.getParecer1());
			arquivoProjeto.getAvaliacao().setDataParecer2(new Date());
		}
		avaliacaoService.inserirAlterar(arquivoProjeto.getAvaliacao());

		if (arquivoProjeto.getAvaliacao().getAvaliador1() != null
				&& arquivoProjeto.getAvaliacao().getAvaliador2() != null) {

			if (!arquivoProjeto.getAvaliacao().getParecer1().equals("PENDENTE")
					&& !arquivoProjeto.getAvaliacao().getParecer2().equals("PENDENTE")) {
				arquivoProjeto.setSituacao(StatusSubmissao.AGUARDANDO_VISTO);
				arquivoProjetoService.inserirAlterar(arquivoProjeto);
				List<Permissao> responsavelCope = daoPermissao.listarCodicaoLivre(Permissao.class,
						" tipo.descricao = 'cope_responsavel' AND status = true");

				for (Permissao p : responsavelCope) {
					util.EnviarEmail.enviarEmail(p.getServidor().getUsuario(), "Avaliadores deram seu parecer",
							"Avaliadores deram seu parecer.");
				}
			}
		} else {
			if (!arquivoProjeto.getAvaliacao().getParecer1().equals("PENDENTE")) {
				arquivoProjeto.setSituacao(StatusSubmissao.AGUARDANDO_VISTO);
				arquivoProjetoService.inserirAlterar(arquivoProjeto);
				List<Permissao> responsavelCope = daoPermissao.listarCodicaoLivre(Permissao.class,
						" tipo.descricao = 'cope_responsavel' AND status = true");

				for (Permissao p : responsavelCope) {
					util.EnviarEmail.enviarEmail(p.getServidor().getUsuario(),
							"Avaliadores deram seu parecer", "Avaliadores deram seu parecer.");
				}
			}
		}

		util.ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscarProjetosPendentes();
		isAvalicao = false;
	}

	public boolean isVerificaPermissaoParaAvaliar() {

		if (arquivoProjeto.getAvaliacao().getAvaliador1() != null
				&& arquivoProjeto.getAvaliacao().getAvaliador2() != null) {

			if (pessoaLogada.getId() == arquivoProjeto.getAvaliacao().getAvaliador1().getId()
					&& arquivoProjeto.getAvaliacao().getParecer1().equals("PENDENTE"))
				return true;

			if (pessoaLogada.getId() == arquivoProjeto.getAvaliacao().getAvaliador2().getId()
					&& arquivoProjeto.getAvaliacao().getParecer2().equals("PENDENTE"))
				return true;

		} else if (arquivoProjeto.getAvaliacao().getAvaliador1() != null) {

			if (pessoaLogada.getId() == arquivoProjeto.getAvaliacao().getAvaliador1().getId()
					&& arquivoProjeto.getAvaliacao().getParecer1().equals("PENDENTE"))
				return true;

		} else {

			if (pessoaLogada.getId() == arquivoProjeto.getAvaliacao().getAvaliador2().getId()
					&& arquivoProjeto.getAvaliacao().getParecer2().equals("PENDENTE"))
				return true;
		}
		return false;
	}



	public List<ArquivoProjeto> getListaArquivoProjetoPendente() {
		return listaArquivoProjetoPendente;
	}

	public void setListaArquivoProjetoPendente(List<ArquivoProjeto> listaArquivoProjetoPendente) {
		this.listaArquivoProjetoPendente = listaArquivoProjetoPendente;
	}

	public ArquivoProjeto getArquivoProjeto() {
		return arquivoProjeto;
	}

	public boolean isAvalicao() {
		return isAvalicao;
	}

	public void setAvalicao(boolean isAvalicao) {
		this.isAvalicao = isAvalicao;
	}

	public void setArquivoProjeto(ArquivoProjeto arquivoProjeto) {
		this.arquivoProjeto = arquivoProjeto;
	}

	public List<Participante> getListaParticipanteProjeto() {
		return listaParticipanteProjeto;
	}

	public void setListaParticipanteProjeto(List<Participante> listaParticipanteProjeto) {
		this.listaParticipanteProjeto = listaParticipanteProjeto;
	}

	public List<ArquivoProjeto> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<ArquivoProjeto> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public List<Criterios> getListaCriterios() {
		return listaCriterios;
	}

	public void setListaCriterios(List<Criterios> listaCriterios) {
		this.listaCriterios = listaCriterios;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

}
