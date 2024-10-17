package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Setor;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class ServidorSetorService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<PessoaSetor> daoPessoaSetor;
	
	@Transacional
	public void inserirAlterar(PessoaSetor setor){
	
		if(setor.getId()==null){
			daoPessoaSetor.inserir(setor);
		}else{
			daoPessoaSetor.alterar(setor);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoPessoaSetor.update(valor);
	}

}
