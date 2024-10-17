package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import protocolo.modelo.Requerimento;
import protocolo.modelo.Setor;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class RequerimentoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Requerimento> daoRequerimento;
	
	@Transacional
	public void inserirAlterar(Requerimento requerimento){
	
		if(requerimento.getId()==null){
			daoRequerimento.inserir(requerimento);
		}else{
			daoRequerimento.alterar(requerimento);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoRequerimento.update(valor);
	}

}
