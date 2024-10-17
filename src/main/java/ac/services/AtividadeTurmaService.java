package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.Atividade;
import ac.modelo.AtividadeTurma;
import base.modelo.Aluno;
import dao.GenericDAO;
import util.Transacional;

public class AtividadeTurmaService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<AtividadeTurma> daoAtividadeTurma;
	
	@Transacional
	public void inserirAlterar(AtividadeTurma atividadeTurma){
		if(atividadeTurma.getId()==null){
			daoAtividadeTurma.inserir(atividadeTurma);
		}else{
			daoAtividadeTurma.alterar(atividadeTurma);
		}
	}

}
