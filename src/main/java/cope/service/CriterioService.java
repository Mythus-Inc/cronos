package cope.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import cope.modelo.Avaliacao;
import cope.modelo.Criterios;
import dao.GenericDAO; 
import util.Transacional;

public class CriterioService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Criterios> dao;
	
	@Transacional
	public void inserirAlterar(Criterios obj){
	
		if(obj.getId()==null){
			dao.inserir(obj);
		}else{
			dao.alterar(obj);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
