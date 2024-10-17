package cope.service;

import java.io.Serializable;

import javax.inject.Inject;

import cope.modelo.TiposProducao;
import dao.GenericDAO; 
import util.Transacional;

public class TiposProducaoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<TiposProducao> dao;
	
	@Transacional
	public void inserirAlterar(TiposProducao obj){
	
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
