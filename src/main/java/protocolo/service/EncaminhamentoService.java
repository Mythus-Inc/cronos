package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import protocolo.modelo.Encaminhamento;
import protocolo.modelo.Setor;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class EncaminhamentoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Encaminhamento> daoEncaminhamento;
	
	@Transacional
	public void inserirAlterar(Encaminhamento encaminhamento){
	
		if(encaminhamento.getId()==null){
			daoEncaminhamento.inserir(encaminhamento);
		}else{
			daoEncaminhamento.alterar(encaminhamento);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoEncaminhamento.update(valor);
	}
	
	@Transacional
	public void remover(Encaminhamento encaminhamento){
		
		System.out.println("encaminhamento asdasdsasd "+encaminhamento);
		daoEncaminhamento.remover(encaminhamento);
	}

}
