
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PagamentoFacade extends AbstractFacade<PagamentoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagamentoFacade() {
        super(PagamentoEntity.class);
    }
    
}

