package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import dao.GenericDAO;
import util.Transacional;

public class MovimentacaoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//@Inject
	//private GenericDAO<Movimentacao> daoMovimentacao;
	
	@Transacional
	public void inserirAlterar( ){
//		if(movimentacao.getId()==null){
//			daoMovimentacao.inserir(movimentacao);
//		}else{
//			daoMovimentacao.alterar(movimentacao);
//		}
	}
	
	@Transacional
	public void update(String valor){
		//daoMovimentacao.update(valor);
	}

}
