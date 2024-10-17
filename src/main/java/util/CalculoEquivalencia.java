package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ac.modelo.Certificado;
import dao.FiltrosDAO;
import dao.GrupoCertificadoDAO;

public class CalculoEquivalencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FiltrosDAO daoFiltros;

	private Double somaCertificado;

	public Double calculo(Double horasMaxima, Double equivalencia1, Double equivalencia2) {
		Double horasCalculadas = 0.0;
		try {
			horasCalculadas = ((horasMaxima * equivalencia1) / equivalencia2);

		} catch (Exception e) {
			System.err.println("CalculoEquivalencia ");
			e.printStackTrace();
		}
		return horasCalculadas;
	}

	public Double calcularHorasCertificado(Certificado certificado) {
		Double horaComputada = 0.0;
		try {
			if ((certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() != null)
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() > 0)) {
				horaComputada = certificado.getAtividadeTurma().getQuantidadeHoraUnica();
			} else if ((!certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getEquivalencia() != null)
					&& (certificado.getAtividadeTurma().getEquivalenciaHora() != null)
					&& (certificado.getAtividadeTurma().getEquivalencia() > 0
							&& certificado.getAtividadeTurma().getEquivalenciaHora() > 0)) {

				horaComputada = calculo(certificado.getQuantidadeMaximaHora(),
						certificado.getAtividadeTurma().getEquivalenciaHora(),
						certificado.getAtividadeTurma().getEquivalencia());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.EQUIVALENCIA_NAO_CADASTRADA);
			}
		} catch (Exception e) {
			System.err.println("Erro calcularHorasCertificado ");
			e.printStackTrace();
		}

		return horaComputada;
	}

	public Double calcularHorasMaximaComputadaCertificado(Double horas, Certificado certificado) {

		List<Certificado> certificados = new ArrayList<>();

		somaCertificado = 0.0;
		certificados = daoFiltros.certificadosAlunosComAtividade(certificado.getAlunoTurma().getId(), 3,
				certificado.getAtividadeTurma().getId());
		somaCertificado = 0.0;
		for (Certificado c : certificados) {
			somaCertificado = somaCertificado + c.getHoraComputada();
		}
 

		Double valorHoras = certificado.getAtividadeTurma().getMaximoHoras() - somaCertificado;

  
		
		if (horas > valorHoras) {
			return valorHoras;
		} else {
			return horas;
		}
		
		

	}

}
