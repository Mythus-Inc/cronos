package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.Grupo;
import base.modelo.Aluno;
import dao.GenericDAO;
import util.Transacional;

public class GrupoService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Grupo> daoGrupo;
	
	@Transacional
	public void inserirAlterar(Grupo grupo){
		if(grupo.getId()==null){
			daoGrupo.inserir(grupo);
		}else{
			daoGrupo.alterar(grupo);
		}
	}

}
