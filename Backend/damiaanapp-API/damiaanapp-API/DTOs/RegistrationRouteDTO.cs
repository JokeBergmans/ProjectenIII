using damiaanapp_API.Models.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.DTOs
{
    public class RegistrationRouteDTO
    {
        #region Properties
        public int Id { get; set; }
        public bool Paid { get; set; }
        public RouteDTO Route { get; set; }
        public Participant Participant { get; set; }
        public Coordinate Position { get; set; }
        public DateTime Timestamp { get; set; }
        public bool TShirt { get; set; }
        public string Tshirtsize { get; set; }
        public ICollection<Scan> Scans { get; set; } = new List<Scan>();


        #endregion

        #region Constructors
        public RegistrationRouteDTO()
        {

        }

        public RegistrationRouteDTO(Registration registration)
        {
            Id = registration.Id;
            Paid = registration.Paid;
            Route = new RouteDTO(registration.Route);
            Participant = registration.Participant;
            Position = registration.Position;
            Timestamp = registration.Timestamp;
            TShirt = registration.TShirt;
            Scans = registration.Scans;
        }
        #endregion
    }
}
