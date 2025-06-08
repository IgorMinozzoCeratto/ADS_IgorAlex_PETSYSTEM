package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ConsultaFacade extends AbstractFacade<ConsultaEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaFacade() {
        super(ConsultaEntity.class);
    }

    private List<ConsultaEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todas as consultas cadastradas
     * @return 
     */
    public List<ConsultaEntity> buscarTodas() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT c FROM ConsultaEntity c ORDER BY c.dataConsulta DESC, c.horaConsulta ASC");
            entityList = (List<ConsultaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar consultas por animal
     * @param animal
     * @return 
     */
    public List<ConsultaEntity> buscarPorAnimal(AnimalEntity animal) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT c FROM ConsultaEntity c WHERE c.animal = :animal ORDER BY c.dataConsulta DESC, c.horaConsulta ASC");
            query.setParameter("animal", animal);
            entityList = (List<ConsultaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar consultas por funcionário
     * @param funcionario
     * @return 
     */
    public List<ConsultaEntity> buscarPorFuncionario(FuncionarioEntity funcionario) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT c FROM ConsultaEntity c WHERE c.funcionario = :funcionario ORDER BY c.dataConsulta DESC, c.horaConsulta ASC");
            query.setParameter("funcionario", funcionario);
            entityList = (List<ConsultaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar consultas por data
     * @param data
     * @return 
     */
    public List<ConsultaEntity> buscarPorData(Date data) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT c FROM ConsultaEntity c WHERE c.dataConsulta = :data ORDER BY c.horaConsulta ASC");
            query.setParameter("data", data);
            entityList = (List<ConsultaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar consultas por período
     * @param dataInicio
     * @param dataFim
     * @return 
     */
    public List<ConsultaEntity> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT c FROM ConsultaEntity c WHERE c.dataConsulta BETWEEN :dataInicio AND :dataFim ORDER BY c.dataConsulta DESC, c.horaConsulta ASC");
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            entityList = (List<ConsultaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
