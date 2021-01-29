using damiaanapp_API.Models.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Models
{
    public interface IRegistrationRepository
    {
        IEnumerable<Registration> GetAll(int routeId);
        Registration GetBy(int id);
        IEnumerable<Registration> GetRegistrationsOfParticipant(int participantId);
        IEnumerable<Registration> GetRegistrationsWithTshirtOrders();
        ICollection<Scan> getScansBy(int id);
        void AddScan(int registrationId, Scan scan);
        void Add(Registration registration);
        void Update(Registration registration);
        void Delete(Registration registration);
        void SaveChanges();
    }
}
