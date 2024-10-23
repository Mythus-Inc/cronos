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
import ac.modelo.Grupo;
import ac.services.GrupoService;

import dao.GenericDAO;

@ViewScoped
@Named("grupoMB")
public class GrupoMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Grupo grupo;
	private List<Grupo> grupos;

	@Inject
	private PermiteInativar permiteInativar;

	@Inject
    private GrupoService grupoService;
	
	@Inject
	private GenericDAO<Grupo> daoGrupo;

	@PostConstruct
	public void inicializar() {
	
		criarNovoObjetoGrupo();
		grupos = new ArrayList<>();
		preencherListaGrupo();
	}

	public void salvar() {
		try {
			if (grupo.getId() == null) {
				grupo.setStatus(true);
				grupo.setDataCadastro(new Date());
				grupoService.inserirAlterar(grupo);
				FecharDialog.fecharDialogGrupo();
				criarNovoObjetoGrupo();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupo();
			} else {
				grupoService.inserirAlterar(grupo);
				FecharDialog.fecharDialogGrupo();
				criarNovoObjetoGrupo();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupo();
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(Grupo grupo) {
	
		try {
			if (permiteInativar.verificarGrupoComGrupoTurma(grupo.getId())) {
				if (permiteInativar.verificarGrupoComAtividade(grupo.getId())) {
					grupo.setStatus(false);
					grupoService.inserirAlterar(grupo);
					preencherListaGrupo();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_COM_ATIVIDADE);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.GRUPO_COM_GRUPO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovoObjetoGrupo() {
		grupo = new Grupo();
	}

	public void preencherListaGrupo() {
		grupos = daoGrupo.listaComStatus(Grupo.class);
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
