package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.Grupo;
import ac.modelo.GrupoTurma;
import base.modelo.Aluno;
import dao.GenericDAO;
import util.Transacional;

public class GrupoTurmaService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;
	
	@Transacional
	public void inserirAlterar(GrupoTurma grupoTurma){
		if(grupoTurma.getId()==null){
			daoGrupoTurma.inserir(grupoTurma);
		}else{
			daoGrupoTurma.alterar(grupoTurma);
		}
	}

}
