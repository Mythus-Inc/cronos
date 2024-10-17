package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.Certificado;
import base.modelo.Aluno; 
import dao.FiltrosDAO;

public class ValidaTopoAtividade implements Serializable {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private List<Certificado> certificados;
	private Double somaCertificado;
	
	@Inject
	private FiltrosDAO daoFiltros;

	public ValidaTopoAtividade() {
		
		certificados = new ArrayList<>();
		somaCertificado = 0.0;
	}

	public Boolean calcularTotalAtividade(Certificado certificado) {
		try {
			
			certificados = new ArrayList<>();
			
			certificados = daoFiltros.certificadosAlunosComAtividade(certificado.getAlunoTurma().getId(), 3,
					certificado.getAtividadeTurma().getId());
			somaCertificado = 0.0;
			for (Certificado c : certificados) {
				somaCertificado = somaCertificado + c.getHoraComputada();
			}

			if ((certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() != null)) {
				if (somaCertificado > certificado.getAtividadeTurma().getQuantidadeHoraUnica()) {
					return false;
				}
			} else if (certificado.getAtividadeTurma().getMaximoHoras() != null) {
				if (somaCertificado >= certificado.getAtividadeTurma().getMaximoHoras()) {
					return false;
				}
			}
		} catch (Exception e) {
			System.err.println("Erro calcularTotalAtividade");
			e.printStackTrace();
		}
		return true;

	}
}
