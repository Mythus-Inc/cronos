package questionario.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ac.modelo.AlunoTurma;
import base.modelo.Aluno;
import base.modelo.Curso;

@Entity
@Table(name = "tab_formulario")
public class Formulario implements Serializable{
	
	
		public Formulario() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_formulario")
		private Long id;
		
		private boolean status;
		
		private String anoConclusao;
		
		private String semestre;
		
		
		
		private String ic2;
		
		private String ic3;
		
		private String ic4;
		
		private String ic5;
		
		private String ic6;
		
		private String ic7;
		
		private String ic8;
		
		private String ic9;
		
		private String ic10;
		
		private String icd1;
		
		private String icd2;
		
		private String icd3;
		
		private String ip1;
		
		private String ip1Complemento;
		
		private String ip2;
		
		private String ip2Complemento;
		
		private String ip3;
		
		private String ip3Complemento;
		
		private String iadc1;
		
		private String iadc2;
		
		private String iadc3;
		
		private String iadc4;
		
		private String iadc5;
		
		private String iadc6;
		
		private String sugestao;
		
		private Date dataResposta;
		
		@ManyToOne
		private AlunoTurma alunoTurma;
		
	
		
		

		public AlunoTurma getAlunoTurma() {
			return alunoTurma;
		}

		public void setAlunoTurma(AlunoTurma alunoTurma) {
			this.alunoTurma = alunoTurma;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public String getAnoConclusao() {
			return anoConclusao;
		}

		public void setAnoConclusao(String anoConclusao) {
			this.anoConclusao = anoConclusao;
		}

		public String getSemestre() {
			return semestre;
		}

		public void setSemestre(String semestre) {
			this.semestre = semestre;
		}

	



		public String getIc2() {
			return ic2;
		}

		public void setIc2(String ic2) {
			this.ic2 = ic2;
		}

		public String getIc3() {
			return ic3;
		}

		public void setIc3(String ic3) {
			this.ic3 = ic3;
		}

		public String getIc4() {
			return ic4;
		}

		public void setIc4(String ic4) {
			this.ic4 = ic4;
		}

		public String getIc5() {
			return ic5;
		}

		public void setIc5(String ic5) {
			this.ic5 = ic5;
		}

		public String getIc6() {
			return ic6;
		}

		public void setIc6(String ic6) {
			this.ic6 = ic6;
		}

		public String getIc7() {
			return ic7;
		}

		public void setIc7(String ic7) {
			this.ic7 = ic7;
		}

		public String getIc8() {
			return ic8;
		}

		public void setIc8(String ic8) {
			this.ic8 = ic8;
		}

		public String getIc9() {
			return ic9;
		}

		public void setIc9(String ic9) {
			this.ic9 = ic9;
		}

		public String getIc10() {
			return ic10;
		}

		public void setIc10(String ic10) {
			this.ic10 = ic10;
		}

		public String getIcd1() {
			return icd1;
		}

		public void setIcd1(String icd1) {
			this.icd1 = icd1;
		}

		public String getIcd2() {
			return icd2;
		}

		public void setIcd2(String icd2) {
			this.icd2 = icd2;
		}

		public String getIcd3() {
			return icd3;
		}

		public void setIcd3(String icd3) {
			this.icd3 = icd3;
		}

		public String getIp1() {
			return ip1;
		}

		public void setIp1(String ip1) {
			this.ip1 = ip1;
		}

		public String getIp1Complemento() {
			return ip1Complemento;
		}

		public void setIp1Complemento(String ip1Complemento) {
			this.ip1Complemento = ip1Complemento;
		}

		public String getIp2() {
			return ip2;
		}

		public void setIp2(String ip2) {
			this.ip2 = ip2;
		}

		public String getIp2Complemento() {
			return ip2Complemento;
		}

		public void setIp2Complemento(String ip2Complemento) {
			this.ip2Complemento = ip2Complemento;
		}

		public String getIp3() {
			return ip3;
		}

		public void setIp3(String ip3) {
			this.ip3 = ip3;
		}

		public String getIp3Complemento() {
			return ip3Complemento;
		}

		public void setIp3Complemento(String ip3Complemento) {
			this.ip3Complemento = ip3Complemento;
		}

		public String getIadc1() {
			return iadc1;
		}

		public void setIadc1(String iadc1) {
			this.iadc1 = iadc1;
		}

		public String getIadc2() {
			return iadc2;
		}

		public void setIadc2(String iadc2) {
			this.iadc2 = iadc2;
		}

		public String getIadc3() {
			return iadc3;
		}

		public void setIadc3(String iadc3) {
			this.iadc3 = iadc3;
		}

		public String getIadc4() {
			return iadc4;
		}

		public void setIadc4(String iadc4) {
			this.iadc4 = iadc4;
		}

		public String getIadc5() {
			return iadc5;
		}

		public void setIadc5(String iadc5) {
			this.iadc5 = iadc5;
		}

		public String getIadc6() {
			return iadc6;
		}

		public void setIadc6(String iadc6) {
			this.iadc6 = iadc6;
		}

		public String getSugestao() {
			return sugestao;
		}

		public void setSugestao(String sugestao) {
			this.sugestao = sugestao;
		}

		public Date getDataResposta() {
			return dataResposta;
		}

		public void setDataResposta(Date dataResposta) {
			this.dataResposta = dataResposta;
		}

	

}
