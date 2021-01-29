using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;

namespace damiaanapp_API.Data.Repositories
{
    public class ParticipantRepository : IParticipantRepository
    {
        #region Fields
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Participant> _participants;
        private readonly IRegistrationRepository _registrationRepository;
        #endregion

        #region Constructors
        public ParticipantRepository(ApplicationDbContext context, IRegistrationRepository registrationRepository)
        {
            _context = context;
            _participants = context.Participants;
            _registrationRepository = registrationRepository;
        }
        #endregion

        #region Methods
        public IEnumerable<Participant> GetAll()
        {
            return _participants.Include(p => p.ParticipantRegistrations).ThenInclude(reg => reg.Registration).OrderBy(p => p.Name).ToList();
        }

        public Participant GetBy(int id)
        {
            return _participants.FirstOrDefault(p => p.Id == id);
        }

        //aparte methode voor het ophalen van volgende registraties, als de includes bij de gewone get zouden gezet worden, zou er kans zijn op circular errors
        public Participant GetByWithIncludes(int id)
        {
            return _participants.Include(p => p.ParticipantRegistrations).ThenInclude(pr => pr.Registration).ThenInclude(p => p.Participant).FirstOrDefault(p => p.Id == id);
        }

        public Participant GetByEmail(string email)
        {
            return _participants.FirstOrDefault(p => p.Email == email);
        }

        public void Add(Participant participant)
        {
            _participants.Add(participant);
        }

        public void Update(Participant participant)
        {
            _participants.Update(participant);
        }

        public void Delete(Participant participant)
        {
            _participants.Remove(participant);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }

        public IEnumerable<Registration> getFollowing(Participant participant)
        {

            List<Registration> followingRegistrations = participant.ParticipantRegistrations.Select(pr => pr.Registration).ToList();
            for(int i=0; i< followingRegistrations.Count; i++)
            {
                Registration reg = followingRegistrations.ElementAt(i);
                if (!reg.Participant.CanBeFollowed)
                {
                    followingRegistrations.Remove(reg);
                }
            }         
            return followingRegistrations;
        }

        public IEnumerable<Route> GetRoutesOfParticipant(int participantId)
        {
            return _registrationRepository.GetRegistrationsOfParticipant(participantId).Select(r => r.Route);
        }
        #endregion
    }
}
