
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProntuarioFacade extends AbstractFacade<ProntuarioEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProntuarioFacade() {
        super(ProntuarioEntity.class);
    }
    
}

