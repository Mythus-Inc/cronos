package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.Pessoa; 
import dao.GenericDAO; 
import util.Transacional;

public class PessoaService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericDAO<Pessoa> daoPessoa;
	
	@Transacional
	public void inserirAlterar(Pessoa pessoa){
	
		if(pessoa.getId()==null){
			daoPessoa.inserir(pessoa);
		}else{
			daoPessoa.alterar(pessoa);
		}
		
	}
	
	
	@Transacional
	public void updateSenha(String senha, String email){
		daoPessoa.updateSenha(senha, email);
	}

}
