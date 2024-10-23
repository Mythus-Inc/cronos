package questionario.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class EmailService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Email> daoEmail;
	
	@Transacional
	public void inserirAlterar(Email email){
	
		if(email.getId()==null){
			daoEmail.inserir(email);
		}else{
			daoEmail.alterar(email);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoEmail.update(valor);
	}

}
