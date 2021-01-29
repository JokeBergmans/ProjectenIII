using damiaanapp_API.Models.Domain;
using System.Collections.Generic;

namespace damiaanapp_API.Models
{
    public interface IParticipantRepository
    {
        IEnumerable<Participant> GetAll();
        Participant GetBy(int id);
        Participant GetByWithIncludes(int id);
        Participant GetByEmail(string email);
        IEnumerable<Registration> getFollowing(Participant participant);
        IEnumerable<Route> GetRoutesOfParticipant(int participantId);
        void Add(Participant participant);
        void Update(Participant participant);
        void Delete(Participant participant);
        void SaveChanges();
    }
}
