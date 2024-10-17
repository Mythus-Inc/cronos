package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import base.modelo.Curso;
import base.modelo.Tipo;
import base.modelo.Turma;
import dao.GenericDAO;
import util.Transacional;

public class CertificadoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Certificado> daoCertificado;
	
	@Transacional
	public void inserirAlterar(Certificado certificado){
		if(certificado.getId()==null){
			daoCertificado.inserir(certificado);
		}else{
			daoCertificado.alterar(certificado);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoCertificado.update(valor);
	}

}
