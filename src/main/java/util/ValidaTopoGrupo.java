package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.Atividade;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.Grupo;
import ac.modelo.GrupoTurma;
import dao.GrupoCertificadoDAO;

public class ValidaTopoGrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Grupo grupo;
	private GrupoTurma grupoTurma;
	private List<Atividade> atividadesParaGrupo;
	private List<Atividade> atividades;
	private List<AtividadeTurma> atividadesTurmas;
	private List<Certificado> certificados;
	private Double somaCertificado;
	private Double somaAtividades;
	private Double somaTotal;
	private List<AtividadeTurma> listAtividadeTurma;

	@Inject
	private GrupoCertificadoDAO daoGrupoCertificado;

	@Inject
	private VerificaSituacaoTurma verificaSituacaoTurma;

	public ValidaTopoGrupo() {

		grupo = new Grupo();
		grupoTurma = new GrupoTurma();
		atividades = new ArrayList<>();
		atividadesParaGrupo = new ArrayList<>();
		atividadesTurmas = new ArrayList<>();
		certificados = new ArrayList<>();
		somaCertificado = 0.0;
		somaAtividades = 0.0;
		somaTotal = 0.0;
	}

	public Boolean calcularTotalGrupo(Certificado certificado) {
		try {

			grupo = new Grupo();
			grupoTurma = new GrupoTurma();
			atividades = new ArrayList<>();
			atividadesParaGrupo = new ArrayList<>();
			atividadesTurmas = new ArrayList<>();
			certificados = new ArrayList<>();

			atividadesParaGrupo = daoGrupoCertificado
					.recuperarGrupoAtividade(certificado.getAtividadeTurma().getAtividade().getId());
			grupo = atividadesParaGrupo.get(0).getGrupo();
			// busco as atividades

			grupoTurma = (GrupoTurma) daoGrupoCertificado
					.recuperarGrupoTurma(certificado.getAtividadeTurma().getMatriz().getId(), grupo.getId()).get(0);
			// seleciona o grupo da turma

			atividades = daoGrupoCertificado.recuperarAtividade(grupoTurma.getGrupo().getId());
			// pega as atividades

			for (Atividade a : atividades) {
				atividadesTurmas.addAll(daoGrupoCertificado.recuperarAtividadeTurmas(a.getId(),
						certificado.getAtividadeTurma().getMatriz().getId()));
			}
			// lista todas as atividades desse dessa matriz

			for (AtividadeTurma a : atividadesTurmas) {
				certificados.addAll(
						daoGrupoCertificado.recuperarCertificados(certificado.getAlunoTurma().getId(), a.getId()));
			}
			// busca os certificados desse aluno

			somaCertificado = 0.0;
			somaAtividades = 0.0;
			somaTotal = 0.0;

			listAtividadeTurma = new ArrayList<>();
			for (Certificado c : certificados) {

				if (listAtividadeTurma.size() == 0) {
					listAtividadeTurma.add(c.getAtividadeTurma());
				} else if (verificaAdd(c.getAtividadeTurma())) {
					listAtividadeTurma.add(c.getAtividadeTurma());
				}

				// somaCertificado = somaCertificado + c.getHoraComputada();
 

			}  
			for(AtividadeTurma atividades : listAtividadeTurma){
				System.out.println("atividade : "+atividades.getAtividade().getDescricao());
				
				for(Certificado cert : certificados){ 
					
					if(cert.getAtividadeTurma().getId().equals(atividades.getId())){
						somaAtividades += cert.getHoraComputada(); 	
					}
					
				}
				
				if(somaAtividades > atividades.getMaximoHoras()){
					somaTotal += atividades.getMaximoHoras();
				}else{
					somaTotal += somaAtividades;
				}
				
				somaAtividades = 0.0;
				
			}
			  

			System.out.println("máximo do grupo = " + grupoTurma.getMaximoHoras());
			System.out.println("computadas = " + somaTotal);

			if (somaTotal >= grupoTurma.getMaximoHoras()) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro calcularTotalGrupo");
			e.printStackTrace();
		}
		return true;
	}

	public boolean verificaAdd(AtividadeTurma atividadeTurma) {

		for (AtividadeTurma a : listAtividadeTurma) {
			if (a.getId().equals(atividadeTurma.getId()))
				return false;

		}
		return true;
	}

}
