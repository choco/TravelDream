package it.polimi.traveldream.ejb;

import it.polimi.traveldream.ejb.dtos.HotelDTO;
import it.polimi.traveldream.ejb.dtos.PacchettoDTO;
import it.polimi.traveldream.ejb.dtos.PacchettoSalvatoDTO;
import it.polimi.traveldream.ejb.entities.*;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cHoco on 27/01/14.
 */
public class UserContentManagerBean implements UserContentManager {

    @PersistenceContext
    private EntityManager em;

    @EJB
    UserManagerBean userManagerBean;

    @Override
    public void prenotaPacchetto(PacchettoSalvatoDTO pacchettoSalvatoDTO) {

    }

    @Override
    public void salvaPacchetto() {

    }

    @Override
    public void personalizzaPacchetto() {

    }

    @Override
    public void aggiungiPartecipazione(PacchettoSalvatoDTO pacchettoSalvatoDTO) {

    }

    @Override
    public PacchettoDTO getPacchetto(PacchettoDTO pacchettoDTO) {
        return null;
    }

    @Override
    public PacchettoSalvatoDTO getPacchettoSalvato() {
        return null;
    }

    @Override
    public List<PacchettoSalvatoDTO> getPacchettiPersonali() {
        long idUserCreatore = userManagerBean.getPrincipalEmail();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PacchettoSalvato> c = cb.createQuery(PacchettoSalvato.class);
        Root<PacchettoSalvato> member = c.from(PacchettoSalvato.class);
        c.select(member).where(cb.equal(member.get("id_userCreatore"), idUserCreatore));
        List<PacchettoSalvato> pacchettiPersonali = em.createQuery(c).getResultList();
        List<PacchettoSalvatoDTO> pacchettiPersonaliDTO = new ArrayList<>();
        for(PacchettoSalvato pacchettoSalvato : pacchettiPersonali) {
            pacchettiPersonaliDTO.add(convertPacchettoSalvatoToDTO(pacchettoSalvato));
        }
        return pacchettiPersonaliDTO;
    }

    @Override
    public List<PacchettoSalvatoDTO> getPacchettiPartecipati() {
        long idUser = userManagerBean.getPrincipalEmail();
        /*CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PacchettoSalvato> c = cb.createQuery(PacchettoSalvato.class);
        Root<PacchettoSalvato> member = c.from(PacchettoSalvato.class);
        Join<>
        c.select(member).where(cb.equal(member. ));
        List<PacchettoSalvato> pacchettiPersonali = em.createQuery(c).getResultList();
        List<PacchettoSalvatoDTO> pacchettiPersonaliDTO = new ArrayList<>();
        for(PacchettoSalvato pacchettoSalvato : pacchettiPersonali) {
            pacchettiPersonaliDTO.add(convertPacchettoSalvatoToDTO(pacchettoSalvato));
        }*/

        TypedQuery query = em.createQuery("select r from PacchettoSalvato r join r.usersPartecipanti p where p.id_user = :idUser", PacchettoSalvato.class).setParameter("idUser", idUser);
        List<PacchettoSalvato> pacchettiPartecipati = query.getResultList();
        List<PacchettoSalvatoDTO> pacchettiPartecipatiDTO = new ArrayList<>();
        for(PacchettoSalvato pacchettoSalvato : pacchettiPartecipati) {
            pacchettiPartecipatiDTO.add(convertPacchettoSalvatoToDTO(pacchettoSalvato));
        }

        return pacchettiPartecipatiDTO;
    }

    @Override
    public List<PacchettoDTO> searchPacchetti(String partenza, String localita, Date dataPartenza, Date dateRitorno) {
        return null;
    }

    private PacchettoDTO convertPacchettoToDTO(Pacchetto pacchetto) {
        PacchettoDTO pacchettoDTO = new PacchettoDTO();
        pacchettoDTO.setNome(pacchetto.getNome());
        pacchettoDTO.setDescrizione(pacchetto.getDescrizione());
        pacchettoDTO.setLocalita(pacchetto.getLocalita());
        pacchettoDTO.setCodice_pacchetto(pacchetto.getCodice_pacchetto());
        pacchettoDTO.setInizioValidita(pacchetto.getInizioValidita());
        pacchettoDTO.setFineValidita(pacchetto.getFineValidita());
        for(TrasportiPacchetto trasportiPacchetto : pacchetto.getMezziTrasporto()) {
            if(trasportiPacchetto.isPredefinito()) {
                pacchettoDTO.addTrasporto(trasportiPacchetto.getTrasporto().getCodice_trasporto(), true);
            }
            else if(!trasportiPacchetto.isPredefinito()) {
                pacchettoDTO.addTrasporto(trasportiPacchetto.getTrasporto().getCodice_trasporto(), false);
            }
        }
        for(HotelsPacchetto hotelsPacchetto : pacchetto.getHotels()) {
            if(hotelsPacchetto.isPredefinito()) {
                pacchettoDTO.addHotel(hotelsPacchetto.getHotel().getCodice_hotel(), true);
            }
            else if(!hotelsPacchetto.isPredefinito()) {
                pacchettoDTO.addHotel(hotelsPacchetto.getHotel().getCodice_hotel(), false);
            }
        }
        for(EscursioniPacchetto escursioniPacchetto : pacchetto.getEscursioni()) {
            if(escursioniPacchetto.isPredefinito()) {
                pacchettoDTO.addEscursione(escursioniPacchetto.getEscursione().getCodice_escursione(), true);
            }
            else if(!escursioniPacchetto.isPredefinito()) {
                pacchettoDTO.addEscursione(escursioniPacchetto.getEscursione().getCodice_escursione(), false);
            }
        }

        return pacchettoDTO;
    }

    private PacchettoSalvatoDTO convertPacchettoSalvatoToDTO(PacchettoSalvato pacchettoSalvato) {
        PacchettoSalvatoDTO pacchettoSalvatoDTO = new PacchettoSalvatoDTO();
        pacchettoSalvatoDTO.setCodicePacchettoOriginale(pacchettoSalvato.getPacchettoOriginale().getCodice_pacchetto());
        pacchettoSalvatoDTO.setIdUserCreatore(pacchettoSalvato.getUserCreatore().getId_user());
        pacchettoSalvatoDTO.setDataPartenza(pacchettoSalvato.getDataPartenza());
        pacchettoSalvatoDTO.setDataRitorno(pacchettoSalvato.getDataRitorno());
        pacchettoSalvatoDTO.setPrenotato(pacchettoSalvato.isPrenotato());
        for(Trasporto trasporto : pacchettoSalvato.getTrasportiScelti()) {
            pacchettoSalvatoDTO.addTrasporto(trasporto.getCodice_trasporto());
        }
        for(Hotel hotel : pacchettoSalvato.getHotelsScelti()) {
            pacchettoSalvatoDTO.addHotel(hotel.getCodice_hotel());
        }
        for(Escursione escursione : pacchettoSalvato.getEscursioni()) {
            pacchettoSalvatoDTO.addEscursione(escursione.getCodice_escursione());
        }

        return pacchettoSalvatoDTO;
    }
}