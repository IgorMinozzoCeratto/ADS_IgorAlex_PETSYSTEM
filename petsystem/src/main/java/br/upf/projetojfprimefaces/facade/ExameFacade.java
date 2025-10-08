package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ExameEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ExameFacade extends AbstractFacade<ExameEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExameFacade() {
        super(ExameEntity.class);
    }

    private List<ExameEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todos os exames cadastrados
     * @return 
     */
    public List<ExameEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT e FROM ExameEntity e ORDER BY e.dataExame DESC");
            entityList = (List<ExameEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar exames por animal
     * @param animal
     * @return 
     */
    public List<ExameEntity> buscarPorAnimal(AnimalEntity animal) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT e FROM ExameEntity e WHERE e.animal = :animal ORDER BY e.dataExame DESC");
            query.setParameter("animal", animal);
            entityList = (List<ExameEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar exames por funcionário
     * @param funcionario
     * @return 
     */
    public List<ExameEntity> buscarPorFuncionario(FuncionarioEntity funcionario) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT e FROM ExameEntity e WHERE e.funcionario = :funcionario ORDER BY e.dataExame DESC");
            query.setParameter("funcionario", funcionario);
            entityList = (List<ExameEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar exames por tipo
     * @param tipoExame
     * @return 
     */
    public List<ExameEntity> buscarPorTipo(String tipoExame) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT e FROM ExameEntity e WHERE LOWER(e.tipoExame) LIKE LOWER(:tipoExame) ORDER BY e.dataExame DESC");
            query.setParameter("tipoExame", "%" + tipoExame + "%");
            entityList = (List<ExameEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar exames por período
     * @param dataInicio
     * @param dataFim
     * @return 
     */
    public List<ExameEntity> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT e FROM ExameEntity e WHERE e.dataExame BETWEEN :dataInicio AND :dataFim ORDER BY e.dataExame DESC");
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            entityList = (List<ExameEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
