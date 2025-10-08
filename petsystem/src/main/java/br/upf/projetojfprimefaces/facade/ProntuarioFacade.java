
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

