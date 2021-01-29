using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Models.Domain
{
    public class ParticipantRegistration
    {
        public int ParticipantId { get; set; }
        public Participant Participant { get; set; }
        public int RegistrationId { get; set; }
        public Registration Registration { get; set; }
    }
}
