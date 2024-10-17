package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import protocolo.modelo.Setor;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class SetorService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Setor> daoSetor;
	
	@Transacional
	public void inserirAlterar(Setor setor){
	
		if(setor.getId()==null){
			daoSetor.inserir(setor);
		}else{
			daoSetor.alterar(setor);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoSetor.update(valor);
	}

}
