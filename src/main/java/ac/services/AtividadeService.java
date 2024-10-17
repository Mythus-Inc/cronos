package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.Atividade;
import base.modelo.Aluno;
import dao.GenericDAO;
import util.Transacional;

public class AtividadeService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Atividade> daoAtividade;
	
	@Transacional
	public void inserirAlterar(Atividade atividade){
		if(atividade.getId()==null){
			daoAtividade.inserir(atividade);
		}else{
			daoAtividade.alterar(atividade);
		}
	}

}
