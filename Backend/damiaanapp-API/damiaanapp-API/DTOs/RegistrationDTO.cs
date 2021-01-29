using damiaanapp_API.Models.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.DTOs
{
    public class RegistrationDTO
    {
        public int Id { get; set; }
        public bool Paid { get; set; }
        public bool TShirt { get; set; }
        public string TshirtSize { get; set; }

        #region Constructors
        public RegistrationDTO()
        {

        }

        public RegistrationDTO(Registration r)
        {
            Id = r.Id;
            Paid = r.Paid;
            TShirt = r.TShirt;
            TshirtSize = r.Tshirtsize;
        }
        #endregion
    }
}
