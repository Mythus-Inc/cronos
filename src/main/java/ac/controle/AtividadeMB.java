package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.modelo.Atividade;
import ac.modelo.Grupo;
import ac.services.AtividadeService;
import dao.GenericDAO;

@ViewScoped
@Named("atividadeMB")
public class AtividadeMB implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private Atividade atividade;
	private List<Atividade> atividades;
	private List<Grupo> grupos;
	
	@Inject
	private PermiteInativar permiteInativar;
	
	@Inject
	private GenericDAO<Atividade> daoAtividade;
	
	@Inject
	private GenericDAO<Grupo> daoGrupo;
	
	@Inject
	private AtividadeService atividadeService;

	@PostConstruct
	public void inicializar() {
	
		criarNovoObjetoAtividade();
		atividades = new ArrayList<>();
		grupos = new ArrayList<>();
		preencherListaAtividade();

	}

	public void salvar() {
		try {
			
			List<Atividade> listaAtividade=daoAtividade.listar(Atividade.class, " descricao='"+ atividade.getDescricao()+ "' and grupo.id='"+atividade.getGrupo().getId()+"'");
			if(listaAtividade.size() > 0){
				ExibirMensagem.exibirMensagem(Mensagem.ATIVIDADE);
			}else{
				
			if (atividade.getId() == null) {
				atividade.setDataCadastro(new Date());
				atividade.setStatus(true);
				atividadeService.inserirAlterar(atividade);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogAtividade();
				criarNovoObjetoAtividade();
				preencherListaAtividade();
			} else {
				atividadeService.inserirAlterar(atividade);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogAtividade();
				criarNovoObjetoAtividade();
				preencherListaAtividade();
			}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Atividade atividade) {
	
		try {
			if (permiteInativar.verificarAtividadeComAtividadeTurma(atividade.getId())) {
				atividade.setStatus(false);
				atividadeService.inserirAlterar(atividade);
				preencherListaAtividade();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.ATIVIDADE_COM_ATIVIDADE_TURMA);
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjetoAtividade() {
		atividade = new Atividade();

	}

	public void preencherListaAtividade() {
		atividades = daoAtividade.listaComStatus(Atividade.class);
	}

	public void preencherListaGrupo() {
		grupos = daoGrupo.listaComStatus(Grupo.class);
	}

	public List<Grupo> completarGrupo(String str) {
		preencherListaGrupo();
		List<Grupo> gruposSelecionados = new ArrayList<>();
		for (Grupo gru : grupos) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				gruposSelecionados.add(gru);
			}
		}
		return gruposSelecionados;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public List<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
