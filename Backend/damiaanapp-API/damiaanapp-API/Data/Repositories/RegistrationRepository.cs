using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Data.Repositories
{
    public class RegistrationRepository : IRegistrationRepository
    {
        #region Fields
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Registration> _registrations;
        #endregion

        #region Constructors
        public RegistrationRepository(ApplicationDbContext context)
        {
            _context = context;
            _registrations = context.Registrations;
        }
        #endregion

        #region Methods
        public IEnumerable<Registration> GetAll(int routeId)
        {
            return _registrations.Include(r => r.Route).Include(r => r.Participant).Include(r => r.Route).Where(r => r.Route.Id == routeId).OrderBy(r => r.Participant.Name).ToList();
        }


        public Registration GetBy(int id)
        {
            return _registrations.Include(r => r.Route).Include(r => r.Participant).Include(r => r.Scans).ThenInclude(sc => sc.Node).FirstOrDefault(r => r.Id == id);
        }
        
        public void Add(Registration registration)
        {
            _registrations.Add(registration);
        }  
        
        public void Update(Registration registration)
        {
            _registrations.Update(registration);
        }

        public void Delete(Registration registration)
        {
            _registrations.Remove(registration);
        }

        public IEnumerable<Registration> GetRegistrationsOfParticipant(int participantId)
        {
            return _registrations.Include(r => r.Route).ThenInclude(ro => ro.Nodes).Include(r => r.Participant).Where(r => r.Participant.Id == participantId).ToList();
        }

        public IEnumerable<Registration> GetRegistrationsWithTshirtOrders()
        {
            return _context.Registrations.Where(r => r.TShirt == true);
        }

        public ICollection<Scan> getScansBy(int id)
        {

            return _context.Registrations.Include(r => r.Scans).ThenInclude(r => r.Node).SingleOrDefault(r => r.Id == id).Scans;
        }

        public void AddScan(int registrationId, Scan scan)
        {
            _context.Registrations.SingleOrDefault(r => r.Id == registrationId).AddScan(scan);
        }
        
        public void SaveChanges()
        {
            _context.SaveChanges();
        }

        #endregion
    }
}
