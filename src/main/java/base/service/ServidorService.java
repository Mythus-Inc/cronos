package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import base.modelo.Servidor;
import dao.GenericDAO;
import util.Transacional;

public class ServidorService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Servidor> daoServidor;
	
	@Transacional
	public void inserirAlterar(Servidor servidor){
		if(servidor.getId()==null){
			daoServidor.inserir(servidor);
		}else{
			daoServidor.alterar(servidor);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoServidor.update(valor);
	}

}
