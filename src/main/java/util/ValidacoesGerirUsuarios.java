package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Pessoa;

import base.modelo.Servidor;
import base.modelo.Tipo; 
import dao.GenericDAO;


public class ValidacoesGerirUsuarios implements Serializable{
	
	
	private static final long serialVersionUID = 1L;


	@Inject
	private GenericDAO<Tipo> daoTipo; //faz as buscas
	
	@Inject
	private GenericDAO<Pessoa> daoPessoa; //faz as buscas
	
	@Inject
	private GenericDAO<Servidor> daoServidor; //faz as buscas
	

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma; //faz as buscas
	

	

	public Boolean buscarUsuarios(Pessoa pessoa) {
		List<Pessoa> pessoas = new ArrayList<>();
		try {
			pessoas = daoPessoa.listarCadastro(Pessoa.class, " status is true and usuario = '" + pessoa.getUsuario() + "'");
			if (pessoas.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarUsuarios");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean buscarPermissao(Tipo tipo) {
		
		
		List<Tipo> tipos = new ArrayList<>();
		try {
			tipos = daoTipo.listar(Tipo.class, " descricao = '" + tipo.getDescricao() + "'");
			if (tipos.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarUsuarios");
			e.printStackTrace();
		}
		return false;
	}
	

	public Boolean buscarUsuarioAlterar(Pessoa pessoa) {
		
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas = daoPessoa.listar(Pessoa.class,
				" usuario = '" + pessoa.getUsuario() + "' and id = '" + pessoa.getId() + "'");
		if (pessoas.size() > 0) {
			return false;
		}
		return true;
	}

	public Boolean buscarSiape(Servidor professor) {
		
		List<Servidor> professores = new ArrayList<>();
		List<Servidor> secretarias = new ArrayList<>();
		try {
			professores = daoServidor.listar(Servidor.class, " siape = '" + professor.getSiape() + "'");
			secretarias = daoServidor.listar(Servidor.class, " siape = '" + professor.getSiape() + "'");
			if (!professores.isEmpty() || !secretarias.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiape ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarSiapeAlterar(Servidor professor) {
	
		List<Servidor> professores = new ArrayList<>();
		try {
			professores = daoServidor.listar(Servidor.class,
					" siape = '" + professor.getSiape() + "' and id = '" + professor.getId() + "'");
			if (professores.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarSiapeAlterar ");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean buscarRA(AlunoTurma alunoTurma) {
		

	    List<AlunoTurma> alunoTurmaCond = new ArrayList<>();
		
		try {
			alunoTurmaCond = daoAlunoTurma.listar(AlunoTurma.class, " ra = " + alunoTurma.getRa().replace(".", "").replace("-", ""));
			if (alunoTurmaCond.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarRA");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean buscarRaAlterar(AlunoTurma aluno) {
	
		List<AlunoTurma> alunos = new ArrayList<>();
		try {
			alunos = daoAlunoTurma.listar(AlunoTurma.class, " ra = '" + aluno.getRa() + "' and id = '" + aluno.getId() + "'");
			if (alunos.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarRaAlterar");
			e.printStackTrace();
		}
		return true;
	}


}
