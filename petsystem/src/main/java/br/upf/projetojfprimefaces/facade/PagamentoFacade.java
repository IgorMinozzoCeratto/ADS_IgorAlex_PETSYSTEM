package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PagamentoFacade extends AbstractFacade<PagamentoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    public PagamentoFacade() {
        super(PagamentoEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /** Cria ou atualiza conforme o ID (compatível com AbstractFacade que retorna void). */
    public void save(PagamentoEntity entity) {
        if (entity.getId() == null) {
            create(entity);   // AbstractFacade#create(T) -> void
        } else {
            edit(entity);     // AbstractFacade#edit(T)   -> void
        }
    }
}
