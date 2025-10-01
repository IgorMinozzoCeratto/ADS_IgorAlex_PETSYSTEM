package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AnimalFacade extends AbstractFacade<AnimalEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnimalFacade() {
        super(AnimalEntity.class);
    }


}
