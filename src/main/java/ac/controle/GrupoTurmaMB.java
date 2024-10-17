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
import util.RecuperarRelacoesProfessor;
import ac.modelo.Grupo;
import ac.modelo.GrupoTurma;
import ac.services.GrupoTurmaService;
import base.modelo.Matriz;
import base.modelo.Turma;
import dao.GenericDAO;

@ViewScoped
@Named("grupoTurmaMB")
public class GrupoTurmaMB implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GrupoTurma grupoTurma;
	private List<Turma> turmas;
	private List<GrupoTurma> grupoTurmas;
	private List<Grupo> grupos;
	
	
	
	@Inject
	private GrupoTurmaService grupoTurmaService;
	
	@Inject
	private GenericDAO<Grupo> daoGrupo;
	
	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;
	
	@Inject
	private GenericDAO<Matriz> daoMatriz;
	
    @Inject
	private RecuperarRelacoesProfessor relacoesProfessor;
    
    @Inject
	private PermiteInativar permiteInativar;

    @PostConstruct
	public void inicializar() {
		
		criarNovoObjetoGrupoTurma();
		turmas = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		grupos = new ArrayList<>();
		
		preencherListaGrupoTurma();
		
	}

	public void salvar() {
	 
		
		try {

			if (grupoTurma.getId() == null) {
				if (verificarGrupoTurmaIguais(grupoTurma)) {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA_CADASTRADO);
				} else {
					if (grupoTurma.getMaximoHoras() >= 0 && grupoTurma.getMinimoHoras() >= 0) {
						if (grupoTurma.getMaximoHoras() >= grupoTurma.getMinimoHoras()) {
							grupoTurma.setDataCadastro(new Date());
							grupoTurma.setStatus(true);
							grupoTurmaService.inserirAlterar(grupoTurma);
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							criarNovoObjetoGrupoTurma();
							preencherListaGrupoTurma();
							FecharDialog.fecharDialogGrupoTurma();

						} else {
							ExibirMensagem.exibirMensagem(Mensagem.MAXIMO_DE_HORAS);
						}
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.CAMPOS_NUMERICOS);
					}
				}
			} else {
				if ((verificarGrupoTurmaIguais(grupoTurma)) && (verificarGrupoTurmaIguaisAlterar(grupoTurma))) {
					ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA_CADASTRADO);
				} else {
					if (grupoTurma.getMaximoHoras() >= 0 && grupoTurma.getMinimoHoras() >= 0) {
						if (grupoTurma.getMaximoHoras() >= grupoTurma.getMinimoHoras()) {
							grupoTurmaService.inserirAlterar(grupoTurma);
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							criarNovoObjetoGrupoTurma();
							preencherListaGrupoTurma();
							FecharDialog.fecharDialogGrupoTurma();
						} else {
							ExibirMensagem.exibirMensagem(Mensagem.MAXIMO_DE_HORAS);
						}
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.CAMPOS_NUMERICOS);
					}
				}
			}

		} catch (

		Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void inativar(GrupoTurma grupoTurma) {
		try {
			if (permiteInativar.verificarGrupoTurmaComAtividadeTurma(grupoTurma)) {
				grupoTurma.setStatus(false);
				grupoTurmaService.inserirAlterar(grupoTurma);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaGrupoTurma();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.GRUPO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public void criarNovoObjetoGrupoTurma() {
		grupoTurma = new GrupoTurma();
	}

	public void preencherListaGrupoTurma() {
		grupoTurmas = daoGrupoTurma.listaComStatus(GrupoTurma.class);
		
	
	}

 

	public void preencherListaGrupo() {
		grupos = daoGrupo.listaComStatus(Grupo.class);
	}

	public Boolean verificarGrupoTurmaIguais(GrupoTurma grupoTurma) {
		try {
			List<GrupoTurma> verificador = new ArrayList<>();
			verificador = daoGrupoTurma.listar(GrupoTurma.class,
					" matriz = " + grupoTurma.getMatriz().getId() + " and grupo = " + grupoTurma.getGrupo().getId());
			if (verificador.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean verificarGrupoTurmaIguaisAlterar(GrupoTurma grupoTurma) {
		try {
			List<GrupoTurma> verificador = new ArrayList<>();
			verificador = daoGrupoTurma.listar(GrupoTurma.class, " matriz = " + grupoTurma.getMatriz().getId() + " and grupo = "
					+ grupoTurma.getGrupo().getId() + " and id = " + grupoTurma.getId());
			if (verificador.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public List<Matriz> completarMatriz(String str) { 
		List<Matriz> listMatriz = daoMatriz.listaComStatus(Matriz.class);
		List<Matriz> listMatrizSelecionada = new ArrayList<>();
		for (Matriz t : listMatriz) {
			if (t.getDescricao().startsWith(str)) {
				listMatrizSelecionada.add(t);
			}
		}
		return listMatrizSelecionada;
	}

	public List<Grupo> completarGrupos(String str) {
		preencherListaGrupo();
		List<Grupo> gruposSelecionados = new ArrayList<>();
		for (Grupo gru : grupos) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				gruposSelecionados.add(gru);
			}
		}
		return gruposSelecionados;
	}

	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}

	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<GrupoTurma> getGrupoTurmas() {
		return grupoTurmas;
	}

	public void setGrupoTurmas(List<GrupoTurma> grupoTurmas) {
		this.grupoTurmas = grupoTurmas;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

}
