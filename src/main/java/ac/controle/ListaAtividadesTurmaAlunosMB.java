package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import dao.FiltrosDAO; 

@ViewScoped
@Named("listaAtividadesTurmaAlunosMB")
public class ListaAtividadesTurmaAlunosMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private AlunoTurma alunoTurma;
	
	@Inject
	private FiltrosDAO daoFiltros;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;


	@PostConstruct
	public void inicializa() {
		alunoTurma = new AlunoTurma();
	

	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}
	
	public void preencherAtividadesTurmas(Long idTurma) {
	
	}

}
