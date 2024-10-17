package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Turma;
import base.service.CertificadoService;
import dao.FiltrosDAO;
import dao.GenericDAO;

public class VerificaSituacaoTurma implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<GrupoTurma> grupoTurmas;
	private List<Certificado> certificados;
	private List<AlunoTurma> alunosTurmas;
	private List<AtividadeTurma> listAtividadeTurma;
	private List<Double> listHoras;
	
	
	@Inject
	private CalculoEquivalencia calculoEquivalencia;

	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;

	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@Inject
	private AlunoService alunoService;
	
	@Inject
	private CertificadoService certificadoService;

	@Inject
	private FiltrosDAO daoFiltros;

	public VerificaSituacaoTurma() {
		grupoTurmas = new ArrayList<>();
		alunosTurmas = new ArrayList<>();
		certificados = new ArrayList<>();

	}

	public void recuperarGrupoTurma(Turma turma) {
		grupoTurmas = daoGrupoTurma.listar(GrupoTurma.class, " matriz = " + turma.getMatriz().getId());
	}

	public void recuperarAlunoTurmas(Turma turma) {
		alunosTurmas = daoFiltros.buscarAlunoTurma(turma.getId());

	}

	public void recuperarCertificados(Turma turma) {
		// busca todos os alunos da turma
		recuperarAlunoTurmas(turma);

		// vai passar um por um dos alunos recuperados
		for (AlunoTurma a : alunosTurmas) {
			
			// aqui busca todos os certificados q foram autenticados / de cada
			// um dos alunos
			certificados.addAll(
					daoCertificado.listar(Certificado.class, " alunoTurma = " + a.getId() + " and situacao = 3"));
			
			if(a.getAluno().getNome().contains("MARESCALCHI")) {
			System.out.println(a.getAluno().getNome()+" - "+certificados.size() + " - "+a.getId());
			for(Certificado ccc:certificados) {
				System.out.println(ccc);
			}
			
			}
			
			// aqui vai mandar calcular a situa��o
			calcularSituacao(turma, a);
			certificados = new ArrayList<>();

		}

	}

	public void recuperarCertificadosAluno(AlunoTurma aluno) {

		certificados.clear();
		certificados.addAll(
				daoCertificado.listar(Certificado.class, " alunoTurma = " + aluno.getId() + " and situacao = 3"));
		// aqui vai mandar calcular a situa��o
		calcularSituacao(aluno.getTurma(), aluno);

	}
	
	public GrupoTurma buscarGrupoTurma(AtividadeTurma atividadeTurma) {
		GrupoTurma grupoTurma = new GrupoTurma();
		try {
			grupoTurma = (GrupoTurma) daoGrupoTurma
					.listar(GrupoTurma.class, " grupo = " + atividadeTurma.getAtividade().getGrupo().getId()
							+ " and matriz = " + atividadeTurma.getMatriz().getId())
					.get(0);
		} catch (Exception e) {
			System.err.println("recuperarGrupoTurma");
			e.printStackTrace();
		}
		return grupoTurma;
	}

	// aqui vem todos os certificados do aluno x "faz isso com todos da turma"
	public void calcularSituacao(Turma turma, AlunoTurma alunoTurma) {
		// aqui manda recuperar todos os grupos dessa turma

		recuperarGrupoTurma(turma);
		List<Boolean> situacoes = new ArrayList<>();
		listHoras = new ArrayList<>();
		listAtividadeTurma = new ArrayList<>();
		Boolean situacaoAux = true;
		Boolean verifica = true;
	
		Double somaTotalHoras = 0.0;
		Double horasComputadas = 0.0;
		Double horasG1 = 0.0;
		Double horasMatriz = 0.0;
		Boolean auxiliaAtividadeTurma = true;

		// aqui ja recuperou os grupos, agora vai passar grupo por grupo
		for (GrupoTurma g : grupoTurmas) {
		
			horasMatriz = g.getMatriz().getTotalHoras();
		}

		for (GrupoTurma g : grupoTurmas) {

			for (Certificado c : certificados) {
				// compara se o id do grupoTurma do certificado � igual ao id do
				// grupoTurma
				// aqui pega todos os certificados do mesmo grupo ->
				
				//System.out.println("idcert: "+c.getIdGrupoTurma());
				//System.out.println("cert: "+c.getDescricao());
				//System.out.println("idgt: "+g.getId());
				if(c.getIdGrupoTurma()==null) {
					
							//System.out.println("ID "+buscarGrupoTurma(c.getAtividadeTurma()).getId());
							c.setIdGrupoTurma(buscarGrupoTurma(c.getAtividadeTurma()).getId());
							certificadoService.inserirAlterar(c);

				}
				
				if(c.getHoraComputada()==null) {
					c.setHoraComputada(
							calculoEquivalencia.calcularHorasCertificado(c));
					certificadoService.inserirAlterar(c);
				}

				if (c.getIdGrupoTurma().equals(g.getId())) {

					// aqui pega as horas desse grupo

					for (AtividadeTurma a : listAtividadeTurma) {
						if (a.getId().equals(c.getAtividadeTurma().getId())) {
							auxiliaAtividadeTurma = false;
						}
						if(c.getId()==19846) {
							System.out.println(c.getAtividadeTurma().getId()+" - "+a.getId());
						}
					}
					if(alunoTurma.getAluno().getNome().contains("MARESCALCHI")) {
						
						System.out.println("auxiliaAtividadeTurma: "+auxiliaAtividadeTurma);
						
					}
					if (auxiliaAtividadeTurma) {
						listAtividadeTurma.add(c.getAtividadeTurma());
					} else {
						auxiliaAtividadeTurma = true;
					}

				}else {
					if(alunoTurma.getAluno().getNome().contains("MARESCALCHI")) {
						System.out.println("FALSEEEEEEE");
					}
				}

			}
			if(alunoTurma.getAluno().getNome().contains("MARESCALCHI")) {
				System.out.println();
				System.out.println(listAtividadeTurma);
			}
			//aqui eu tenho as atividades que a pessoa tem cadastrada, agora esse m�todo pega cada uma dessas atividades e vai buscar
			//nos certificados elas, verificando se ta essas atividade bate a hr maxima por atividade ou n�o. 
			for (AtividadeTurma ativ : listAtividadeTurma) {
				
				horasG1 += buscarHoras(ativ, alunoTurma);
				horasComputadas += buscarHorasTotais(ativ);
			}
			somaTotalHoras += horasG1;

			listHoras.add(horasG1);
			listHoras.add(horasComputadas);

			 
			if (horasG1 < g.getMinimoHoras()) {
				verifica = false;
			}
			horasG1 = 0.;
			horasComputadas = 0.;
			listAtividadeTurma = new ArrayList<>();
		}

		alterarHoras(alunoTurma);

		if (verifica) {
			if (verificaSituacao(somaTotalHoras, horasMatriz)) {
				alterarSituacao(alunoTurma, true);
			} else {
				alterarSituacao(alunoTurma, false);
			}

		} else {
			alterarSituacao(alunoTurma, false);
		}
		somaTotalHoras = 0.0;
		verifica = true;
		listHoras = new ArrayList<>();

	}

	public void alterarHoras(AlunoTurma aluno) {

		for (int i = 0; i < 1; i++) {
			alunoService.update(" Aluno set totalHorasCadastradasG1 = '" + listHoras.get(1)
					+ "' , totalHorasComputadasG1 = '" + listHoras.get(0) + "' , totalHorasCadastradasG2 = '"
					+ listHoras.get(3) + "' , totalHorasComputadasG2 = '" + listHoras.get(2)
					+ "' , totalHorasCadastradasG3 = '" + listHoras.get(5) + "' , totalHorasComputadasG3 = '"
					+ listHoras.get(4) + "'  where id = " + aluno.getAluno().getId());
		}

	}

	public Boolean verificaSituacao(Double horasTotais, Double horasMatriz) {

		if (horasTotais >= horasMatriz) {
			return true;
		} else {
			return false;
		}

	}

	public Double buscarHoras(AtividadeTurma atividade, AlunoTurma al) {
		Double horasComputadas = 0.;

		for (Certificado c : certificados) {
			if(al.getAluno().getNome().contains("MARESCALCHI")){
				System.out.println("AAA");
				System.out.println(c);
				System.out.println(atividade.getAtividade().getDescricao()+" - "+c.getAtividadeTurma().getAtividade().getDescricao());
			}
			if (atividade.getId().equals(c.getAtividadeTurma().getId())) {
				horasComputadas += c.getHoraComputada();
			}
		}
		
		if (horasComputadas > atividade.getMaximoHoras()) {
			return atividade.getMaximoHoras();
		} else {
			return horasComputadas;
		}
	}

	public Double buscarHorasTotais(AtividadeTurma atividade) {
		Double horasComputadas = 0.;
		for (Certificado c : certificados) {
			if (atividade.getId().equals(c.getAtividadeTurma().getId())) {
				horasComputadas += c.getHoraComputada();
			}
		}

		return horasComputadas;

	}

	public void alterarSituacao(AlunoTurma alunoTurma, Boolean situacao) {
		if (situacao) {
			alunoService.update(" Aluno set situacao = 'Completou' where id = " + alunoTurma.getAluno().getId());
		} else {
			alunoService.update(" Aluno set situacao = 'Não Completou' where id = " + alunoTurma.getAluno().getId());
		}
	}
}
