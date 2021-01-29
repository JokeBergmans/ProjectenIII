using damiaanapp_API.Data.Repositories;
using damiaanapp_API.DTOs;
using damiaanapp_API.Models.Domain.Enum;
using System;
using System.Collections.Generic;
using System.Linq;

namespace damiaanapp_API.Models.Domain
{
    public class Registration
    {
        #region Properties
        public int Id { get; set; }
        public bool Paid { get; set; }
        public Route Route { get; set; }
        public Participant Participant { get; set; }
        public Coordinate Position { get; set; }
        public DateTime Timestamp { get; set; }
        public bool TShirt { get; set; }
        public string Tshirtsize { get; set; }
        public ICollection<Scan> Scans { get; set; } = new List<Scan>();


        #endregion

        #region Constructors
        public Registration()
        {

        }

        public Registration(Route route, Participant participant)
        {
            Route = route;
            Participant = participant;
            Timestamp = DateTime.Now;
            Paid = false;
            TShirt = false;
        }
        #endregion

        #region Methods

        public void UpdateFromDTO(RegistrationDTO registrationDTO)
        {
            Paid = registrationDTO.Paid;
            TShirt = registrationDTO.TShirt;
            Tshirtsize = registrationDTO.TshirtSize;
        }

        public void AddScan(Scan scan)
        {
            Scans.Add(scan); 
        }
        #endregion

    }
}
