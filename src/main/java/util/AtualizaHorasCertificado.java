package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Atividade;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import base.modelo.Tipo;
import base.service.AlunoTurmaService;
import base.service.CertificadoService; 
import dao.GenericDAO;

public class AtualizaHorasCertificado implements Serializable{
	
	
	private static final long serialVersionUID = 1L;


	private List<Certificado> certificados;
	private List<Atividade> atividadesCertificados;
	private List<AtividadeTurma> atividadeTurmas;
	
	
	@Inject
	private GenericDAO<Certificado> daoCertificado; 
	
	@Inject
	private GenericDAO<AtividadeTurma> daoAtividadeTurma; 
	
	@Inject
	private CertificadoService certificadoService;
	
	@Inject
	private CalculoEquivalencia calculoEquivalencia; 
	

	public AtualizaHorasCertificado() {
		
		certificados = new ArrayList<>();
		atividadesCertificados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		
	}

	public void buscarCertificados(AlunoTurma alunoTurma) {
		try {
			certificados = new ArrayList<>();
			certificados = (List<Certificado>) daoCertificado.listar(Certificado.class,
					" aluno = " + alunoTurma.getAluno().getId());
		} catch (Exception e) {
			System.err.println("buscarCertificados");
			e.printStackTrace();
		}
	}

	public void verificarAtividades(AlunoTurma alunoTurma) {
		try {
			buscarCertificados(alunoTurma);
			for (int i = 0; i <= certificados.size() - 1; i++) {
				atividadesCertificados.add(certificados.get(i).getAtividadeTurma().getAtividade());
			}
		} catch (Exception e) {
			System.err.println("Erro em verificarAtividades");
			e.printStackTrace();
		}
	}

	public void buscarAtividades(AlunoTurma alunoTurma) {
		try {
			atividadeTurmas = new ArrayList<>();
			verificarAtividades(alunoTurma);

			for (int i = 0; i <= atividadesCertificados.size() - 1; i++) {
				atividadeTurmas.addAll(daoAtividadeTurma.listar(AtividadeTurma.class, " atividade = "
						+ atividadesCertificados.get(i).getId() + " and turma = " + alunoTurma.getTurma().getId()));

			}
		} catch (Exception e) {
			System.err.println("Erro em buscarAtividades:");
			e.printStackTrace();
		}
	}

	public void alterarHoras(AlunoTurma alunoTurma) {
		try {
			buscarCertificados(alunoTurma);
			if (certificados.size() > 0) {
				buscarAtividades(alunoTurma);
				buscarCertificados(alunoTurma);
				certificadoService.update(" Certificado set atualizado = false where aluno = " + alunoTurma.getAluno().getId());

				for (int i = 0; i <= certificados.size() - 1; i++) {
					for (int c = 0; c <= atividadeTurmas.size() - 1; c++) {
						if (certificados.get(i).getAtividadeTurma().getAtividade().getId() == atividadeTurmas.get(c)
								.getAtividade().getId()) {

							certificadoService.update(" Certificado set atividadeTurma = " + atividadeTurmas.get(c).getId()
									+ " where id = " + certificados.get(i).getId());

							certificados.get(i).setHoraComputada(
									calculoEquivalencia.calcularHorasCertificado(certificados.get(i)));
							certificadoService.inserirAlterar(certificados.get(i));

							certificadoService.update(" Certificado set atualizado = true where id = " + certificados.get(i).getId());
						}
					}
				}
				certificadoService.update(" Certificado set status = false where aluno = " + alunoTurma.getAluno().getId()
						+ " and atualizado = false ");

			}
		} catch (

		Exception e) {
			System.err.println("Erro alterarHoras");
			e.printStackTrace();
		}
	}

}
