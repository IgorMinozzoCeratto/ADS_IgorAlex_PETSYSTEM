
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

