package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.VacinacaoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class VacinacaoFacade extends AbstractFacade<VacinacaoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VacinacaoFacade() {
        super(VacinacaoEntity.class);
    }

    private List<VacinacaoEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todas as vacinações cadastradas
     * @return 
     */
    public List<VacinacaoEntity> buscarTodas() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT v FROM VacinacaoEntity v ORDER BY v.dataAplicacao DESC");
            entityList = (List<VacinacaoEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar vacinações por animal
     * @param animal
     * @return 
     */
    public List<VacinacaoEntity> buscarPorAnimal(AnimalEntity animal) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT v FROM VacinacaoEntity v WHERE v.animal = :animal ORDER BY v.dataAplicacao DESC");
            query.setParameter("animal", animal);
            entityList = (List<VacinacaoEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar vacinações por funcionário (veterinário)
     * @param funcionario
     * @return 
     */
    public List<VacinacaoEntity> buscarPorFuncionario(FuncionarioEntity funcionario) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT v FROM VacinacaoEntity v WHERE v.funcionario = :funcionario ORDER BY v.dataAplicacao DESC");
            query.setParameter("funcionario", funcionario);
            entityList = (List<VacinacaoEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar vacinações por tipo de vacina
     * @param tipoVacina
     * @return 
     */
    public List<VacinacaoEntity> buscarPorTipoVacina(String tipoVacina) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT v FROM VacinacaoEntity v WHERE LOWER(v.tipoVacina) LIKE LOWER(:tipoVacina) ORDER BY v.dataAplicacao DESC");
            query.setParameter("tipoVacina", "%" + tipoVacina + "%");
            entityList = (List<VacinacaoEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar vacinações por período
     * @param dataInicio
     * @param dataFim
     * @return 
     */
    public List<VacinacaoEntity> buscarPorPeriodo(Date dataInicio, Date dataFim) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT v FROM VacinacaoEntity v WHERE v.dataAplicacao BETWEEN :dataInicio AND :dataFim ORDER BY v.dataAplicacao DESC");
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            entityList = (List<VacinacaoEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
